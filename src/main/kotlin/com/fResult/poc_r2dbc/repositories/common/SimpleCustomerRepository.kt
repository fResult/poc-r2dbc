package com.fResult.poc_r2dbc.repositories.common

import com.fResult.poc_r2dbc.entities.Customer
import org.springframework.data.repository.NoRepositoryBean
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@NoRepositoryBean
interface SimpleCustomerRepository {
  fun save(customer: Customer): Mono<Customer>

  fun findAll(): Flux<Customer>

  fun update(customer: Customer): Mono<Customer>

  fun findById(id: String): Mono<Customer>

  fun deleteById(id: String): Mono<Void>
}
