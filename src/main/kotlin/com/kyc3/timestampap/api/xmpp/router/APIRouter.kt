package com.kyc3.timestampap.api.xmpp.router

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class APIRouter(
  private val listeners: List<APIListener<*, *>>,
) {
  private val log = LoggerFactory.getLogger(javaClass)

  fun route(message: Message.SignedMessage, chat: Chat): Mono<out GeneratedMessageV3> =
    try {
      when (message.bodyCase) {
        Message.SignedMessage.BodyCase.ADDRESSED ->
          Mono.justOrEmpty(
            listeners.find { listener -> message.addressed.message.`is`(listener.type()) }
              .also {
                if (it == null) {
                  log.warn("" +
                      "process='OracleAPIListener.listenToOracle' " +
                      "message='Type can't be processed' " +
                      "type=${message.addressed.message.typeUrl}")
                }
              }
              ?.also { log.info("process='OracleRouter.route' type='${it.type()}'") }
              ?.let { Pair(it, message) }
          )
            .flatMap {
              try {
                it.first.accept(it.second.addressed.message, chat)
              } catch (ex: RuntimeException) {
                log.warn("Exception during message processing", ex)
                Mono.error(ex)
              }
            }
        Message.SignedMessage.BodyCase.ANONYMOUS ->
          log.warn("Can't handle anonymous messages")
            .let { Mono.empty() }
        else -> Mono.error(IllegalStateException())
      }
    } catch (ex: RuntimeException) {
      log.warn("Exception during message processing", ex)
      Mono.empty()
    }
}