package com.fResult.poc_r2dbc.routers

import com.fResult.poc_r2dbc.handlers.OrderHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class OrderRouter(private val handler: OrderHandler) {
  @Bean
  fun orderRoutes(): RouterFunction<ServerResponse> = coRouter {
    "/fe/orders".nest {
      POST("", handler::createOrders)
    }
  }
}
