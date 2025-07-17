package com.fResult.poc_r2dbc.handlers

import com.fResult.poc_r2dbc.CustomerUpdateRequest
import com.fResult.poc_r2dbc.entities.Customer
import com.fResult.poc_r2dbc.repositories.mongodb.CustomerRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.kotlin.core.publisher.toMono
import java.net.URI

@Component
class CustomerHandler(private val repository: CustomerRepository) {
  suspend fun all(request: ServerRequest): ServerResponse =
    repository.findAll()
      .collectList()
      .flatMap { ServerResponse.ok().bodyValue(it) }.awaitSingle()

  suspend fun byId(request: ServerRequest): ServerResponse =
    request.pathVariable("id")
      .let { repository.findById(it) }
      .flatMap { ServerResponse.ok().bodyValue(it) }
      .switchIfEmpty(ServerResponse.notFound().build())
      .awaitSingle()

  suspend fun create(request: ServerRequest): ServerResponse =
    request.bodyToMono<Customer>()
      .flatMap(repository::save)
      .flatMap { ServerResponse.created(URI.create("/customers/${it.id}")).bodyValue(it) }
      .awaitSingle()

  suspend fun update(request: ServerRequest): ServerResponse {
    val id = request.pathVariable("id")
    return request.bodyToMono<CustomerUpdateRequest>()
      .zipWith(repository.findById(id)) { body, existing ->
        Customer(existing.id, body.email ?: existing.email)
      }
      .map { existing -> Customer(existing.id, existing.email) }
      .flatMap(repository::save)
      .flatMap { ServerResponse.ok().bodyValue(it) }
  }

  suspend fun deleteById(request: ServerRequest): ServerResponse {
    return request.pathVariable("id")
      .toMono()
      .flatMap(repository::deleteById)
      .then(ServerResponse.noContent().build())
      .awaitSingle()
  }
}
