package com.kyc3.timestampap.config

import com.kyc3.timestampap.config.properties.DatasourceProperties
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate


@Configuration
class DatabaseConfiguration(
  val datasourceProperties: DatasourceProperties
) {

  @Bean
  fun connectionFactory(): ConnectionFactory =
    ConnectionFactories.get(
      ConnectionFactoryOptions.builder()
        .option(DRIVER, "postgresql")
        .option(HOST, datasourceProperties.host)
        .option(PORT, datasourceProperties.port)
        .option(USER, datasourceProperties.username)
        .option(PASSWORD, datasourceProperties.password)
        .option(DATABASE, datasourceProperties.database)
        .build()
    )

  @Bean
  fun r2dbcEntityTemplate(connectionFactory: ConnectionFactory): R2dbcEntityTemplate =
    R2dbcEntityTemplate(connectionFactory)
}