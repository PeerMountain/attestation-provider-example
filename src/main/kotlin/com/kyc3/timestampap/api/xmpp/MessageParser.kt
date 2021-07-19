package com.kyc3.timestampap.api.xmpp

import org.springframework.stereotype.Service
import org.jivesoftware.smack.packet.Message

@Service
class MessageParser {

  fun parseMessage(message: Message): ByteArray =
      message.body.split(",").map { it.toUByte().toByte() }.toByteArray()

}