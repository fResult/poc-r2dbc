package com.fResult.poc_r2dbc.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Customer(@Id val id: String?, val email: String) {
  constructor(email: String) : this(null, email)
}
