package com.fResult.poc_r2dbc.repositories.common

import com.fResult.poc_r2dbc.Customer
import com.fResult.poc_r2dbc.repositories.springdata.CustomerRepository
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Profile("common")
class CommonCustomerRepository(
  private val dbClient: DatabaseClient,
  private val environment: Environment,
) : SimpleCustomerRepository {

  private val rowMapper: (Map<String, Any>) -> Customer = { row -> Customer(row["id"] as Int, row["email"] as String) }

  companion object {
    private val log: Logger = LogManager.getLogger(CustomerRepository::class.java)
  }

  init {
    (environment.activeProfiles
      .takeIf(Array<out String>::isNotEmpty)
      ?.joinToString()
      ?: "common")
      .also { profile ->
        log.info(
          "[{}] initialized with profile: [{}]",
          CommonCustomerRepository::class.simpleName,
          profile
        )
      }
  }

  override fun save(customer: Customer): Mono<Customer> {
    return dbClient.sql("INSERT INTO customer ( email ) values ( $1 )")
      .bind("$1", customer.email)
      .filter { stmt, _ -> stmt.returnGeneratedValues("id").execute() }
      .fetch()
      .first()
      .flatMap { findById(it["id"] as Int) }
  }

  override fun findAll(): Flux<Customer> =
    dbClient.sql("SELECT * FROM customer")
      .fetch()
      .all()
      .`as` { it.map(rowMapper) }

  override fun update(customer: Customer): Mono<Customer> =
    dbClient.sql("UPDATE customer SET email = $2 WHERE id = $1")
      .bind("$1", customer.id!!)
      .bind("$2", customer.email)
      .fetch()
      .first()
      .switchIfEmpty(Mono.empty())
      .then(findById(customer.id))

  override fun findById(id: Int): Mono<Customer> =
    dbClient.sql("SELECT * FROM customer WHERE id = $1")
      .bind("$1", id)
      .fetch()
      .first()
      .map(rowMapper)

  override fun deleteById(id: Int): Mono<Void> =
    dbClient.sql("DELETE FROM customer WHERE id = $1")
      .bind("$1", id)
      .fetch()
      .rowsUpdated()
      .then()
}
