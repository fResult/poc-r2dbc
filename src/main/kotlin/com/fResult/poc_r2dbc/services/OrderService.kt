package com.fResult.poc_r2dbc.services

import com.fResult.poc_r2dbc.entities.Order
import org.bson.assertions.Assertions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class OrderService(private val template: ReactiveMongoTemplate, private val operator: TransactionalOperator) {
  fun createOrders(vararg productIds: String): Flux<Order> =
    operator.execute { transaction -> buildOrderFlux(template::insert, productIds) }

  fun buildOrderFlux(
    createOrder: (Order) -> Mono<Order>,
    orderIds: Array<out String>
  ): Flux<Order> = Flux.just(*orderIds)
    .doOnEach { orderIdSignal ->
      Assertions.notNull(orderIdSignal.get(), "Order ID must not be null")
    }
    .map { Order(it) }
    .flatMap(createOrder)
}
