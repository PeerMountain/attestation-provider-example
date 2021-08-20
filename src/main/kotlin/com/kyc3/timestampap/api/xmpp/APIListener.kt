package com.kyc3.timestampap.api.xmpp

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import com.kyc3.timestampap.api.xmpp.router.APIRouter
import org.jivesoftware.smack.chat2.ChatManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import javax.annotation.PostConstruct

@Service
class APIListener(
  private val chatManager: ChatManager,
  private val oracleRouter: APIRouter,
  private val messageParser: MessageParser,
  private val oracleApiResponse: OracleAPIResponse,
  private val signatureVerificationService: SignatureVerificationService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostConstruct
  fun listenToOracle() {
    chatManager.addIncomingListener { from, message, chat ->
      log.info("process='OracleAPIListener.listenToOracle' from='${from.asUnescapedString()}' message='received an event'")
      doIfValid(from.asEntityBareJidString(), messageParser.parseMessage(message)) { signed ->
        oracleRouter.route(signed, chat)
      }
        .subscribe {
          oracleApiResponse.responseToClient(chat, it)
        }
    }
  }

  fun doIfValid(
    from: String,
    message: Message.SignedMessage,
    consumer: (Message.SignedMessage) -> Mono<out GeneratedMessageV3>
  ): Mono<GeneratedMessageV3> =
    Mono.justOrEmpty<GeneratedMessageV3>(signatureVerificationService.verify(from, message))
      .switchIfEmpty(consumer(message))
}