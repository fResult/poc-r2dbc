package com.fResult.poc_r2dbc.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Order(@Id val id: String, val productId: String)
