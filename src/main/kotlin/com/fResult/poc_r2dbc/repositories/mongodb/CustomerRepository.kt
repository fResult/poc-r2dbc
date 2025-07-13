package com.fResult.poc_r2dbc.repositories.mongodb

import com.fResult.poc_r2dbc.entities.Customer
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.mongodb.repository.Tailable
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository("reactiveMongoCustomerRepository")
interface CustomerRepository : ReactiveMongoRepository<Customer, String> {
  @Tailable
  fun findByEmail(email: String): Flux<Customer>
}
