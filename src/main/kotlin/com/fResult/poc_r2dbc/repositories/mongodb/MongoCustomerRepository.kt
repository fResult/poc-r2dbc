package com.fResult.poc_r2dbc.repositories.mongodb

import com.fResult.poc_r2dbc.entities.Customer
import com.fResult.poc_r2dbc.repositories.common.SimpleCustomerRepository
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Profile("mongo")
@Repository("mongoCustomerRepository")
class MongoCustomerRepository(
  @Qualifier("reactiveMongoCustomerRepository") private val repository: CustomerRepository,
  private val environment: Environment,
) : SimpleCustomerRepository {

  companion object {
    val log: Logger = LogManager.getLogger(MongoCustomerRepository::class.java)
  }

  init {
    (environment.activeProfiles
      .takeIf(Array<out String>::isNotEmpty)
      ?.joinToString()
      ?: "common")
      .also { profile ->
        log.info(
          "[{}] initialized with profile: [{}]",
          MongoCustomerRepository::class.simpleName,
          profile
        )
      }
  }

  override fun save(customer: Customer): Mono<Customer> = repository.save(customer)

  override fun findAll(): Flux<Customer> = repository.findAll()

  override fun update(customer: Customer): Mono<Customer> = repository.save(customer)

  override fun findById(id: String): Mono<Customer> = repository.findById(id)

  override fun deleteById(id: String): Mono<Void> = repository.deleteById(id)
}
