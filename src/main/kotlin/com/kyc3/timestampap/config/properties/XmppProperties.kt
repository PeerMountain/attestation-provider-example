package com.kyc3.timestampap.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "xmpp")
class XmppProperties {
  lateinit var domain: String
  lateinit var password: String
  lateinit var host: String
}