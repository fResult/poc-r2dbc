package com.fResult.poc_r2dbc

import com.fResult.poc_r2dbc.repositories.common.SimpleCustomerRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI

@RestController
@RequestMapping("/customers")
class CustomerController(private val repository: SimpleCustomerRepository) {
  @GetMapping
  fun all(): Flux<Customer> {
    return repository.findAll()
  }

  @GetMapping("/{id}")
  fun byId(@PathVariable id: String): Mono<ResponseEntity<Customer>> {
    return Mono.just(id)
      .flatMap(repository::findById)
      .map { customer -> ResponseEntity.ok(customer) }
      .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
  }

  @PostMapping
  fun create(@RequestBody customer: Mono<Customer>): Mono<ResponseEntity<Customer>> {
    return customer.flatMap(repository::save)
      .map {
        ResponseEntity.created(URI("/customers/${it.id}")).body(it)
      }
  }

  @PatchMapping("/{id}")
  fun update(@PathVariable id: String, @RequestBody customer: Customer): Mono<Customer> {
    return Mono.just(id).flatMap(repository::findById)
      .flatMap { exists -> repository.save(Customer(exists.id, customer.email)) }
      .switchIfEmpty(Mono.error(IllegalArgumentException("Customer with id [${id}] not found")))
  }

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: String): Mono<Void> {
    return Mono.just(id).flatMap(repository::deleteById)
  }
}
