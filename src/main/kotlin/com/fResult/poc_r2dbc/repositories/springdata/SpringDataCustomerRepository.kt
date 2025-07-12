package com.fResult.poc_r2dbc.repositories.springdata

import com.fResult.poc_r2dbc.Customer
import com.fResult.poc_r2dbc.repositories.common.SimpleCustomerRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Profile("springdata")
class SpringDataCustomerRepository(
  private val repository: CustomerRepository,
) :
  SimpleCustomerRepository {
  override fun save(customer: Customer): Mono<Customer> = repository.save(customer)

  override fun findAll(): Flux<Customer> = repository.findAll()

  override fun update(customer: Customer): Mono<Customer> = repository.save(customer)

  override fun findById(id: Int): Mono<Customer> = repository.findById(id)

  override fun deleteById(id: Int): Mono<Void> = repository.deleteById(id)
}
