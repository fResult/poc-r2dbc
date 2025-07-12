package com.fResult.poc_r2dbc

import com.fResult.poc_r2dbc.repositories.common.SimpleCustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CustomerRepositoryTest : BaseCustomerRepositoryTest() {
  @Autowired
  private lateinit var customerRepository: SimpleCustomerRepository

  override fun getRepository(): SimpleCustomerRepository {
    return customerRepository
  }
}
