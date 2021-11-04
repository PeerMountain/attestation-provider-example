package com.kyc3.timestampap.config

import com.kyc3.timestampap.config.properties.XmppProperties
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.c2s.ModularXmppClientToServerConnection
import org.jivesoftware.smack.c2s.ModularXmppClientToServerConnectionConfiguration
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.websocket.XmppWebSocketTransportModuleDescriptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.web3j.crypto.Credentials

@Configuration
@Profile("!test")
class XMPPConfiguration(
  val xmppProperties: XmppProperties,
) {

  @Bean
  fun connectionConfiguration(
    credentials: Credentials
  ): ModularXmppClientToServerConnectionConfiguration =
    ModularXmppClientToServerConnectionConfiguration.builder()
      .also {
        it.removeAllModules()
      }
      .setXmppDomain(xmppProperties.domain)
      .setUsernameAndPassword(credentials.address, xmppProperties.password)
      .setHost(xmppProperties.host)
      .also {
        it.addModule(
          XmppWebSocketTransportModuleDescriptor.getBuilder(
            it
          )
            .explicitlySetWebSocketEndpoint("wss://xmpp.kyc3.com/xmpp-websocket")
            .build()
        )
      }
      .build()

  @Bean
  fun connection(
    connectionConfiguration: ModularXmppClientToServerConnectionConfiguration
  ): XMPPConnection =
    ModularXmppClientToServerConnection(connectionConfiguration)
      .also {
        it.connect()
        it.login()
      }

  @Bean
  fun chatManager(connection: XMPPConnection): ChatManager =
    ChatManager.getInstanceFor(connection)

}