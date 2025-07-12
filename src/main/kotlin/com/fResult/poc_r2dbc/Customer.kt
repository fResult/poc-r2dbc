package com.fResult.poc_r2dbc

import org.springframework.data.annotation.Id

data class Customer(@Id val id: Int?, val email: String) {
  constructor(email: String) : this(null, email)
}
