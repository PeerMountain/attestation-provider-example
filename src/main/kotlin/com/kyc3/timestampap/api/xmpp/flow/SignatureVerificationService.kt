package com.kyc3.timestampap.api.xmpp.flow

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.ErrorDtoOuterClass
import com.kyc3.Message
import com.kyc3.timestampap.service.Web3JService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class SignatureVerificationService(
    private val web3Service: Web3JService,
    private val encoder: Base64.Encoder
) {

    fun verify(
        from: String,
        message: Message.SignedMessage
    ): Mono<GeneratedMessageV3> =
        when (message.bodyCase) {
            Message.SignedMessage.BodyCase.ANONYMOUS -> Mono.error(RuntimeException("Can't handle anonymous messages"))
            Message.SignedMessage.BodyCase.ADDRESSED ->
                if (!validSignature(from, message)) {
                    if (messageIsError(message)) {
                        Mono.empty()
                    } else {
                        Mono.just(
                            ErrorDtoOuterClass.ErrorDto.newBuilder()
                                .setMessage("Invalid message signature")
                                .build()
                        )
                    }
                } else {
                    Mono.empty()
                }
            else ->
                Mono.just(
                    ErrorDtoOuterClass.ErrorDto.newBuilder()
                        .setMessage("Invalid message signature")
                        .build()
                )
        }

    private fun messageIsError(message: Message.SignedMessage) =
        message.addressed.message.`is`(ErrorDtoOuterClass.ErrorDto::class.java)

    private fun validSignature(from: String, message: Message.SignedMessage) = web3Service.verifySignature(
        encoder.encodeToString(message.addressed.message.toByteArray()),
        message.addressed.signature,
      from
    )
}
