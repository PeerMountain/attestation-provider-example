package com.kyc3.timestampap.api.xmpp.router

import com.google.protobuf.Any
import com.kyc3.oracle.ap.Register
import com.kyc3.timestampap.service.OracleService
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RegisterAttestationProviderListener(
    private val oracleService: OracleService
) :
    APIListener<Register.RegisterAttestationProviderResponse, Register.RegisterAttestationProviderResponse> {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun type(): Class<Register.RegisterAttestationProviderResponse> =
        Register.RegisterAttestationProviderResponse::class.java

    override fun accept(event: Any, chat: Chat): Mono<Register.RegisterAttestationProviderResponse> {
        log.info("process=RegisterAttestationProviderListener message=$event")
        oracleService.requestNftSettingsList()
        return Mono.empty()
    }
}