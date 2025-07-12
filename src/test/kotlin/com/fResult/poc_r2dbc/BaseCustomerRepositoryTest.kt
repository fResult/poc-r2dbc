package com.fResult.poc_r2dbc

import com.fResult.poc_r2dbc.repositories.common.SimpleCustomerRepository
import org.junit.jupiter.api.Test
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@Testcontainers
abstract class BaseCustomerRepositoryTest {
  abstract fun getRepository(): SimpleCustomerRepository

//  @Autowired
//  private lateinit var initializer: CustomerDatabaseInitializer

//  @Value("classpath:/schema.sql")
//  private lateinit var resource: Resource

  companion object {
    @DynamicPropertySource
    fun registerProperties(registry: DynamicPropertyRegistry) {
      registry.add("spring.sql.init.mode") { "always" }
      registry.add("spring.r2dbc.url") { "r2dbc:tc:postgresql:///orders?TC_IMAGE_TAG=17.5-alpine" }
      registry.add("spring.r2dbc.username") { "postgres" }
      registry.add("spring.r2dbc.password") { "postgres" }
    }
  }

  @Test
  fun delete() {
    val repository = getRepository()
    val customers = repository.findAll()
      .flatMap { repository.findById(it.id!!) }
      .thenMany(
        Flux.just(
          Customer(null, "first@email.com"),
          Customer(null, "second@email.com"),
          Customer(null, "third@email.com"),
        )
      )
      .flatMap(repository::save)

    StepVerifier.create(customers).expectNextCount(3).verifyComplete()
    StepVerifier.create(repository.findAll().take(1).flatMap { repository.deleteById(it.id!!) }.then())
      .verifyComplete()
    StepVerifier.create(repository.findAll()).expectNextCount(2).verifyComplete()
  }

  @Test
  fun `save and find all`() {
    val repository = getRepository()
    val customers = Flux.just(
      Customer(null, "first@email.com"),
      Customer(null, "second@email.com"),
      Customer(null, "third@email.com"),
    )
      .flatMap(repository::save)

    StepVerifier.create(customers)
      .expectNextCount(2)
      .expectNextMatches { it.id != null }
      .verifyComplete()
  }

  @Test
  fun `find by id`() {
    val repository = getRepository()
    var customers = Flux.just(
      Customer(null, "first@email.com"),
      Customer(null, "second@email.com"),
      Customer(null, "third@email.com"),
    )
      .flatMap(repository::save)

    StepVerifier.create(customers).expectNextCount(3).verifyComplete()

    val recordsById = repository.findAll()
      .flatMap { Mono.zip(Mono.just(it), repository.findById(it.id!!)) }
      .filterWhen { Mono.just(it.t1 == it.t2) }

    StepVerifier.create(recordsById).expectNextCount(3).verifyComplete()
  }

  @Test
  fun update() {
    val repository = getRepository()
    val email = "test@again.com"

    StepVerifier.create(
      repository.findAll()
        .flatMap { repository.deleteById(it.id!!) }
        .flatMap<Customer> { repository.save(Customer(null, email.uppercase())) }
    )
      .expectNextMatches { it.id != null }
      .verifyComplete()

    StepVerifier.create(repository.findAll())
      .expectNextCount(1)
      .verifyComplete()

    StepVerifier.create(
      repository.findAll()
        .map { Customer(it.id!!, it.email.lowercase())}
        .flatMap(repository::update)
    )
      .expectNextMatches { it.email == email.lowercase() }
      .verifyComplete()
  }
}