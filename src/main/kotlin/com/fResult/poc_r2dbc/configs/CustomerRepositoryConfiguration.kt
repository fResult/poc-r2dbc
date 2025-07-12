package com.fResult.poc_r2dbc.configs

import com.fResult.poc_r2dbc.repositories.common.CommonCustomerRepository
import com.fResult.poc_r2dbc.repositories.common.SimpleCustomerRepository
import com.fResult.poc_r2dbc.repositories.springdata.CustomerRepository
import com.fResult.poc_r2dbc.repositories.springdata.SpringDataCustomerRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient

@Configuration
class CustomerRepositoryConfiguration {
  @Bean
  @Profile("common")
  fun commonCustomerRepository(dbClient: DatabaseClient): SimpleCustomerRepository = CommonCustomerRepository(dbClient)

  @Bean
  @Profile("springdata")
  fun springDataCustomerRepository(repository: CustomerRepository): SimpleCustomerRepository =  SpringDataCustomerRepository(repository)
}
