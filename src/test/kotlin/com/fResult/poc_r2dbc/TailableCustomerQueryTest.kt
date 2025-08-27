package com.fResult.poc_r2dbc

import com.fResult.poc_r2dbc.entities.Customer
import com.fResult.poc_r2dbc.entities.Order
import com.fResult.poc_r2dbc.repositories.mongodb.CustomerRepository
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.CollectionOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

@DataMongoTest
@Testcontainers
class TailableCustomerQueryTest {
  companion object {
    const val EMAIL = "KornZilla@example.com"
    val log: Logger = LogManager.getLogger(TailableCustomerQueryTest::class.java)

    @Container
    val mongoDbContainer: MongoDBContainer = MongoDBContainer("mongo:8.0-rc-noble")

    @JvmStatic
    @DynamicPropertySource
    fun setProperties(registry: DynamicPropertyRegistry) {
      log.info("URL: {}", mongoDbContainer.replicaSetUrl)
      registry.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl)
    }
  }

  @Autowired
  lateinit var operations: ReactiveMongoTemplate

  @Autowired
  lateinit var repository: CustomerRepository

  @BeforeEach
  fun setUp() {
    val capped = CollectionOptions.empty().size(1024 * 1024).maxDocuments(100).capped()
    val recreateCollection = operations.collectionExists(Customer::class.java)
      .flatMap { exists -> if (exists) operations.dropCollection(Customer::class.java) else Mono.just(exists) }
      .then(operations.createCollection(Customer::class.java, capped))

    StepVerifier.create(recreateCollection).expectNextCount(1).verifyComplete()
  }

  @Test
  fun tail() {
    val customers = ConcurrentLinkedQueue<Customer>()

    StepVerifier.create(write().then(write()))
      .expectNextCount(1)
      .verifyComplete()

    repository.findByEmail(EMAIL)
      .doOnNext(customers::add)
      .doOnComplete { log.info("Completed") }
      .doOnTerminate { log.info("Terminated") }
      .subscribe()

    Thread.sleep(500)
    assertEquals(2, customers.size)

    StepVerifier.create(write().then(write())).expectNextCount(1).verifyComplete()

    Thread.sleep(500)
    assertEquals(4, customers.size)
  }

  private fun write(): Mono<Customer> = repository.save(Customer(UUID.randomUUID().toString(), EMAIL))
}
