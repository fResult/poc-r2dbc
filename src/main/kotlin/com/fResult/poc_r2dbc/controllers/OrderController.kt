package com.fResult.poc_r2dbc.controllers

import com.fResult.poc_r2dbc.services.OrderService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rc/orders")
class OrderController(private val service: OrderService) {
  @PostMapping
  fun createOrders(@RequestBody orderIds: List<String>) = service.createOrders(*orderIds.toTypedArray())
}
