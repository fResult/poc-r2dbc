package com.fResult.poc_r2dbc

import com.fResult.poc_r2dbc.repositories.common.SimpleCustomerRepository
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.util.FileCopyUtils
import java.io.InputStreamReader

@Component
class CustomerDatabaseInitializer(
  @Value("classpath:/schema.sql") private val resource: Resource,
  private val dbClient: DatabaseClient,
  private val repository: SimpleCustomerRepository,
  private val txnOperator: TransactionalOperator,
) {
  private val sql: String

  init {
    InputStreamReader(resource.inputStream).use { inputStreamReader ->
      sql = FileCopyUtils.copyToString(inputStreamReader)
    }
  }

  fun resetCustomerTable(): Publisher<Void> {
    val createSchema = dbClient.sql(sql).then()
    val findAndDelete = repository.findAll().flatMap { repository.deleteById(it.id!!) }

    return createSchema.thenMany(txnOperator.execute { _ -> findAndDelete })
  }
}
