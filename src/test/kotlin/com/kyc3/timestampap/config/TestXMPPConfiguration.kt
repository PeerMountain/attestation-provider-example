package com.kyc3.timestampap.config

import com.kyc3.timestampap.Constants
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.c2s.ModularXmppClientToServerConnectionConfiguration
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jxmpp.jid.impl.JidCreate
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestXMPPConfiguration {

    @Bean
    @Primary
    fun testConnectionConfiguration(): ModularXmppClientToServerConnectionConfiguration =
        Mockito.mock(ModularXmppClientToServerConnectionConfiguration::class.java)

    @Bean
    @Primary
    fun testConnection(): XMPPConnection = Mockito.mock(XMPPConnection::class.java)

    @Bean
    @Primary
    fun testChatManager(connection: XMPPConnection): ChatManager =
        Mockito.mock(ChatManager::class.java)
            .also {
                `when`(
                    it.chatWith(
                        JidCreate.entityBareFrom(
                            "${Constants.ORACLE_ADDRESS}@xmpp.kyc3.com"
                        )
                    )
                )
                    .thenReturn(Mockito.mock(Chat::class.java))
            }

}