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
    if (!validSignature(message)) {
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

  private fun messageIsError(message: Message.SignedMessage) =
    message.message.`is`(ErrorDtoOuterClass.ErrorDto::class.java)

  private fun validSignature(message: Message.SignedMessage) = web3Service.verifySignature(
    encoder.encodeToString(message.message.toByteArray()),
    message.signature,
    message.address
  )
}