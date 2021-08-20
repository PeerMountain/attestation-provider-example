package com.kyc3.timestampap.api.xmpp

import com.kyc3.ErrorDtoOuterClass
import com.kyc3.Message
import com.kyc3.timestampap.service.Web3JService
import org.springframework.stereotype.Service
import java.util.*

@Service
class SignatureVerificationService(
  private val web3JService: Web3JService,
  private val encoder: Base64.Encoder
) {

  fun verify(
    from: String,
    message: Message.SignedMessage
  ): ErrorDtoOuterClass.ErrorDto? =
    if (!validSignature(message)) {
      ErrorDtoOuterClass.ErrorDto.newBuilder()
        .setMessage("Invalid message signature")
        .build()
    } else {
      null
    }

  private fun validSignature(message: Message.SignedMessage) = web3JService.verifySignature(
    encoder.encodeToString(message.message.toByteArray()),
    message.signature,
    message.address
  )
}