package com.fResult.poc_r2dbc.repositories.springdata

import com.fResult.poc_r2dbc.Customer
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface CustomerRepository : R2dbcRepository<Customer, Int>
