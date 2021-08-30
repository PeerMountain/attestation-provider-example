package com.kyc3.timestampap.api.xmpp.flow

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import com.kyc3.timestampap.api.xmpp.OracleAPIResponse
import com.kyc3.timestampap.api.xmpp.router.APIRouter
import org.jivesoftware.smack.chat2.Chat
import org.jxmpp.jid.EntityBareJid
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class EncryptedMessageFlow(
  private val apiRouter: APIRouter,
  private val messageParser: MessageParser,
  private val messageDecryptService: EncryptionService,
  private val oracleAPIResponse: OracleAPIResponse,
  private val signatureVerificationService: SignatureVerificationService,
) {

  fun encryptedMessage(from: EntityBareJid, chat: Chat, message: Message.EncryptedMessage) {
    val decryptedMessage = messageDecryptService.decryptMessage(message)
    val signedMessage = messageParser.parseSignedMessage(String(decryptedMessage))

    doIfValid(from.asEntityBareJidString(), signedMessage) { signed ->
      apiRouter.route(signed, chat)
    }
      .subscribe { oracleAPIResponse.responseToClient(signedMessage.publicKey, chat, it) }
  }

  fun doIfValid(
    from: String,
    message: Message.SignedMessage,
    consumer: (Message.SignedMessage) -> Mono<out GeneratedMessageV3>
  ): Mono<out GeneratedMessageV3> =
    signatureVerificationService.verify(from, message)
      .switchIfEmpty(consumer(message))
}