package com.kyc3.timestampap.api.xmpp

import org.springframework.stereotype.Service
import org.jivesoftware.smack.packet.Message
import java.util.*

@Service
class MessageParser(
  private val base64Decoder: Base64.Decoder
) {

  fun parseMessage(message: Message): ByteArray =
    base64Decoder.decode(message.body)

}