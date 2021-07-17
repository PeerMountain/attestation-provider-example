package com.kyc3.timestampap.config

import com.kyc3.timestampap.config.properties.DatasourceProperties
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfiguration {

  @Bean
  fun dataSource(datasourceProperties: DatasourceProperties): PGSimpleDataSource =
    DataSourceBuilder.create()
      .driverClassName("org.postgresql.Driver")
      .type(PGSimpleDataSource::class.java)
      .url("jdbc:postgresql://${datasourceProperties.host}:${datasourceProperties.port}/${datasourceProperties.database}")
      .username(datasourceProperties.username)
      .password(datasourceProperties.password)
      .build()
}