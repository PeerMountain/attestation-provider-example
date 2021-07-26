package com.kyc3.timestampap.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@ConfigurationProperties(prefix = "oracle.redirect")
@Component
class OracleUrlProperties {
  lateinit var baseUrl: String
}