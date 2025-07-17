package com.fResult.poc_r2dbc.handlers

import com.fResult.poc_r2dbc.entities.Customer
import com.fResult.poc_r2dbc.repositories.mongodb.CustomerRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import java.net.URI

@Component
class CustomerHandler(private val repository: CustomerRepository) {
  suspend fun create(request: ServerRequest): ServerResponse =
    request.bodyToMono<Customer>()
      .flatMap(repository::save)
      .flatMap { ServerResponse.created(URI.create("/customers/${it.id}")).bodyValue(it) }
      .awaitSingle()
}
