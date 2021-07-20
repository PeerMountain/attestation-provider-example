package com.kyc3.timestampap.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@ConfigurationProperties(prefix = "attestation.redirect")
@Component
class AttestationUrlProperties {
  lateinit var baseUrl: String
}