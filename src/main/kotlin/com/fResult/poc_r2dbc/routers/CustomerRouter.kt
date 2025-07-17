package com.fResult.poc_r2dbc.routers

import com.fResult.poc_r2dbc.handlers.CustomerHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class CustomerRouter(private val handler: CustomerHandler) {
  @Bean
  fun customerRoutes() = coRouter {
    "/fe/customers".nest {
      GET("", handler::all)
      POST("", handler::create)
    }
  }
}
