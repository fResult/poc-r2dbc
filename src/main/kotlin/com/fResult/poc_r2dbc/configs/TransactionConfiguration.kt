package com.fResult.poc_r2dbc.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.ReactiveMongoTransactionManager
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.reactive.TransactionalOperator

@Configuration
@EnableTransactionManagement
class TransactionConfiguration {
  @Bean
  fun transactionOperator(transactionManager: ReactiveTransactionManager): TransactionalOperator =
    TransactionalOperator.create(transactionManager)

  @Bean
  fun reactiveMongoTransactionManager(
    databaseFactory: ReactiveMongoDatabaseFactory,
  ): ReactiveTransactionManager = ReactiveMongoTransactionManager(databaseFactory)
}
