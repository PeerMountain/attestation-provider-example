package com.kyc3.timestampap.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("oracle")
class OracleProperties {
    lateinit var address: String
}