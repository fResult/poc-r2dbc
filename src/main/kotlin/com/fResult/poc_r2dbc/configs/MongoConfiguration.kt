package com.fResult.poc_r2dbc.configs

import com.fResult.poc_r2dbc.toList
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.boot.mongodb.autoconfigure.MongoProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

@Configuration
class MongoConfiguration(private val mongoProps: MongoProperties) {
  @Bean
  fun mongoClient(): MongoClient {
    val credential =
      MongoCredential.createCredential(
        mongoProps.username!!,
        mongoProps.authenticationDatabase!!,
        mongoProps.password!!,
      )
    val settings = MongoClientSettings.builder()
      .applyToClusterSettings {
        ServerAddress(mongoProps.host, mongoProps.port!!)
          .let(ServerAddress::toList)
          .let(it::hosts)
      }
      .credential(credential)
      .retryWrites(false)
      .build()

    return MongoClients.create(settings)
  }

  @Bean
  fun reactiveMongoTemplate(mongoClient: MongoClient): ReactiveMongoTemplate {
    return ReactiveMongoTemplate(mongoClient, mongoProps.database!!)
  }
}
