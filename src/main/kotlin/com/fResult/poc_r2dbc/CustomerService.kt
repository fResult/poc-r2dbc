package com.fResult.poc_r2dbc

import com.fResult.poc_r2dbc.repositories.common.SimpleCustomerRepository
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Predicate

@Service
class CustomerService(
  private val repository: SimpleCustomerRepository,
  private val operator: TransactionalOperator,
  private val dbInitializer: CustomerDatabaseInitializer,
) {
  fun resetDatabase(): Publisher<Void> = dbInitializer.resetCustomerTable()

  fun upsert(email: String): Flux<Customer> {

    return repository
      .findAll()
      .filter(sameCustomerEmailIgnoreCase(email))
      .flatMap { repository.update(Customer(it.id, email)) }
      .switchIfEmpty(repository.save(Customer(null, email)))
      .filter(::validateEmail)
      .switchIfEmpty(displayIllegalArgumentError())
      .let(operator::transactional)

  }

  private fun sameCustomerEmailIgnoreCase(email: String): Predicate<Customer> =
    Predicate { it.email.equals(email, ignoreCase = true) }

  private fun validateEmail(customer: Customer): Boolean = customer.email.contains("@")

  private fun displayIllegalArgumentError(): Mono<Customer> =
    Mono.error(IllegalArgumentException("The email needs to be of the form [email@example.com]!"))
}
