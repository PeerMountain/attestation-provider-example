package com.kyc3.timestampap.api.xmpp

import com.google.protobuf.Any
import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import com.kyc3.timestampap.service.SignatureHelper
import com.kyc3.timestampap.service.Web3JService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Service
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Hash
import org.web3j.crypto.Keys
import org.web3j.utils.Numeric
import java.util.*

@Service
class OracleAPIResponse(
  private val base64Encoder: Base64.Encoder,
  private val ecKeyPair: ECKeyPair,
  private val web3JService: Web3JService
) {

  fun responseToClient(chat: Chat, message: GeneratedMessageV3) =
    Any.pack(message)
      .let {
        Message.SignedMessage.newBuilder()
          .setMessage(it)
          .setAddress(Keys.getAddress(ecKeyPair))
          .setSignature(SignatureHelper.toString(web3JService.sign(encodeMessage(it))))
          .build()
      }
      .toByteArray()
      .let { base64Encoder.encodeToString(it) }
      .let { chat.send(it) }

  fun encodeMessage(message: Any): String =
    base64Encoder.encodeToString(message.toByteArray())
}
