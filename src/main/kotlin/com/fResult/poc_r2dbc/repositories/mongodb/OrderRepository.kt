package com.fResult.poc_r2dbc.repositories.mongodb

import com.fResult.poc_r2dbc.entities.Order
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface OrderRepository : ReactiveMongoRepository<Order, String> {
  fun findByProductId(productId: String): Flux<Order>
}
