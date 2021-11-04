package com.kyc3.timestampap.api.xmpp.router

import com.google.protobuf.Any
import com.kyc3.oracle.ap.Data
import com.kyc3.timestampap.service.NftSettingsSyncService
import com.kyc3.timestampap.service.OracleService
import com.kyc3.timestampap.service.RegistrationService
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.web3j.crypto.Credentials
import reactor.core.publisher.Mono

@Component
class AttestationProviderDataListener(
    private val registrationService: RegistrationService,
    private val oracleService: OracleService,
    private val credentials: Credentials
) : APIListener<Data.AttestationProviderDataResponse, Data.AttestationProviderDataResponse> {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun type(): Class<Data.AttestationProviderDataResponse> = Data.AttestationProviderDataResponse::class.java

    override fun accept(event: Any, chat: Chat): Mono<Data.AttestationProviderDataResponse> =
        event.unpack(type())
            .let {
                if (it.provider.name.isBlank()) {
                    log.info("process=AttestationProviderDataListener message='starting registration'")
                    registrationService.register(credentials.address)
                } else {
                    log.info("process=AttestationProviderDataListener providerName=${it.provider.name}")
                    oracleService.requestNftSettingsList()
                }
                Mono.empty()
            }
}
