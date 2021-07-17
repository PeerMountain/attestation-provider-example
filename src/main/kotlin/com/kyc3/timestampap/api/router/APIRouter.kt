package com.kyc3.timestampap.api.router

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.MessageOuterClass
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class APIRouter(
  private val listeners: List<APIListener<*, *>>,
) {
  private val log = LoggerFactory.getLogger(javaClass)

  fun route(byteArray: ByteArray, chat: Chat): Mono<out GeneratedMessageV3> =
    Mono.fromSupplier { MessageOuterClass.Message.parseFrom(byteArray) }
      .mapNotNull { message ->
        listeners.find { listener -> message.message.`is`(listener.type()) }
          .also {
            if (it == null) {
              log.warn("process='OracleAPIListener.listenToOracle' message='Type can't be processed'")
            }
          }
          ?.let { Pair(it, message) }
      }
      .flatMap {
        try {
          it.first.accept(it.second.message, chat)
        } catch (ex: RuntimeException) {
          log.warn("Exception during message processing", ex)
          Mono.error(ex)
        }
      }
}