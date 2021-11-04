package com.kyc3.timestampap.api.xmpp.router

import com.google.protobuf.Any
import com.kyc3.Exchange
import com.kyc3.timestampap.model.UserKeys
import com.kyc3.timestampap.service.OracleService
import com.kyc3.timestampap.service.UserKeysService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ExchangeKeysResponseListener(
    private val userKeysService: UserKeysService,
    private val oracleService: OracleService
) : APIListener<Exchange.ExchangeKeysResponse, Exchange.ExchangeKeysResponse> {
    override fun type(): Class<Exchange.ExchangeKeysResponse> =
        Exchange.ExchangeKeysResponse::class.java

    override fun accept(event: Any, chat: Chat): Mono<Exchange.ExchangeKeysResponse> =
        event.unpack(type())
            .let {
                userKeysService.store(
                    chat.xmppAddressOfChatPartner.asEntityBareJidString(),
                    UserKeys(
                        username = it.username,
                        publicEncryptionKey = it.publicEncryptionKey,
                        address = it.address
                    )
                )
                oracleService.requestAttestationProviderData()
            }
            .let {
                Mono.empty()
            }

}