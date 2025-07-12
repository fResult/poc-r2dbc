package com.fResult.poc_r2dbc

import com.fResult.poc_r2dbc.repositories.common.SimpleCustomerRepository
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.transaction.reactive.TransactionalOperator

@Configuration
class CommonAutoConfiguration {
  companion object {
    val log: Logger = LogManager.getLogger(CommonAutoConfiguration::class.java)
  }

  @Profile("common")
  @Bean
  fun commonCustomerService(
    @Qualifier("commonCustomerRepository") customerRepository: SimpleCustomerRepository,
    transactionOperator: TransactionalOperator,
    dbInitializer: CustomerDatabaseInitializer,
  ): CustomerService {
    return CustomerService(customerRepository, transactionOperator, dbInitializer)
  }

  @Profile("common")
  @Bean
  fun demoCommon(@Qualifier("commonCustomerRepository") repository: SimpleCustomerRepository): ApplicationListener<ApplicationReadyEvent> =
    ApplicationListener { event ->
      repository.save(Customer(null, "fResult@exampl.com"))
        .thenMany(repository.findAll())
        .doOnEach(log::info)
        .blockFirst()
    }
}
