package com.fResult.poc_r2dbc.configs

import com.fResult.poc_r2dbc.repositories.common.CommonCustomerRepository
import com.fResult.poc_r2dbc.repositories.common.SimpleCustomerRepository
import com.fResult.poc_r2dbc.repositories.mongodb.MongoCustomerRepository
import com.fResult.poc_r2dbc.repositories.springdata.CustomerRepository as R2dbcCustomerRepository
import com.fResult.poc_r2dbc.repositories.springdata.SpringDataCustomerRepository
import org.springframework.beans.factory.annotation.Qualifier
import com.fResult.poc_r2dbc.repositories.mongodb.CustomerRepository as ReactiveMongoCustomerRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.r2dbc.core.DatabaseClient

@Configuration
class CustomerRepositoryConfiguration {
  @Bean
  @Profile("common")
  fun commonCustomerRepository(
    dbClient: DatabaseClient,
    environment: Environment,
  ): SimpleCustomerRepository = CommonCustomerRepository(dbClient, environment)

  @Bean
  @Profile("mongo")
  fun mongoCustomerRepository(
    @Qualifier("reactiveMongoCustomerRepository") repository: ReactiveMongoCustomerRepository,
    environment: Environment,
  ): SimpleCustomerRepository = MongoCustomerRepository(repository, environment)

  @Bean
  @Profile("springdata")
  fun springDataCustomerRepository(
    repository: R2dbcCustomerRepository,
    environment: Environment,
  ): SimpleCustomerRepository = SpringDataCustomerRepository(repository, environment)
}
