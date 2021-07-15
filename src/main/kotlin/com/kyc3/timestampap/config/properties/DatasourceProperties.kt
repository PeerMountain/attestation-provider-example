package com.kyc3.timestampap.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "datasource")
@Component
class DatasourceProperties {
  lateinit var host: String
  var port: Int = 0
  lateinit var username: String
  lateinit var password: String
  lateinit var database: String
}