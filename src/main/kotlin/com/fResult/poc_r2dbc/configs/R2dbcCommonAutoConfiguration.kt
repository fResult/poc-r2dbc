package com.fResult.poc_r2dbc.configs

import com.fResult.poc_r2dbc.CustomerDatabaseInitializer
import com.fResult.poc_r2dbc.services.CustomerService
import com.fResult.poc_r2dbc.entities.Customer
import com.fResult.poc_r2dbc.repositories.common.SimpleCustomerRepository
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.transaction.reactive.TransactionalOperator

@Configuration
class R2dbcCommonAutoConfiguration {
  companion object {
    val log: Logger = LogManager.getLogger(R2dbcCommonAutoConfiguration::class.java)
  }

  @Bean
  @Profile("springdata", "common")
  fun commonCustomerService(
    customerRepository: SimpleCustomerRepository,
    transactionOperator: TransactionalOperator,
    dbInitializer: CustomerDatabaseInitializer,
  ): CustomerService {
    log.info("Initializing commonCustomerService with common configuration")
    return CustomerService(customerRepository, transactionOperator, dbInitializer)
  }

  @Bean
  @Profile("springdata", "common")
  fun demoCommon(repository: SimpleCustomerRepository): ApplicationListener<ApplicationReadyEvent> {

    log.info("Registering demoCommon listener")
    return ApplicationListener { event ->
      repository.save(Customer("fResult@r2dbc.com"))
        .thenMany(repository.findAll())
        .doOnEach(log::info)
        .blockFirst()
    }
  }
}
