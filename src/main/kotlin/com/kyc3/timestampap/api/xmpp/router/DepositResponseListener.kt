package com.kyc3.timestampap.api.xmpp.router

import com.google.protobuf.Any
import com.kyc3.oracle.ap.AttestationProviderOuterClass
import com.kyc3.oracle.ap.Register
import com.kyc3.oracle.user.Deposit
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component
import org.web3j.crypto.Credentials
import reactor.core.publisher.Mono

@Component
class DepositResponseListener(
    private val credentials: Credentials
) :
    APIListener<Deposit.DepositResponse, Register.RegisterAttestationProviderRequest> {
    override fun type(): Class<Deposit.DepositResponse> = Deposit.DepositResponse::class.java

    override fun accept(event: Any, chat: Chat): Mono<Register.RegisterAttestationProviderRequest> =
        event.unpack(type())
            .let {
                Mono.just(
                    Register.RegisterAttestationProviderRequest.newBuilder()
                        .setProvider(
                            AttestationProviderOuterClass.AttestationProvider.newBuilder()
                                .setAddress(credentials.address)
                                .setName(credentials.address)
                                .setInitialTransaction(it.depositTransactionHash)
                                .build()
                        )
                        .build()
                )
            }
}