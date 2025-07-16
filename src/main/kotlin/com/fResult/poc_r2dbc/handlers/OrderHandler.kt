package com.fResult.poc_r2dbc.handlers

import com.fResult.poc_r2dbc.services.OrderService
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.net.URI

@Component
class OrderHandler(private val service: OrderService) {
  suspend fun createOrders(request: ServerRequest): ServerResponse =
    service.createOrders(*request.awaitBody<Array<out String>>())
      .collectList()
      .awaitSingle()
      .let { ServerResponse.created(URI.create("/rc/orders")).bodyValueAndAwait(it) }
}
