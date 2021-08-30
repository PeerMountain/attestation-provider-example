package com.kyc3.timestampap.api.xmpp

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import com.kyc3.timestampap.api.xmpp.flow.IncomingMessageManager
import com.kyc3.timestampap.api.xmpp.router.APIRouter
import org.jivesoftware.smack.chat2.ChatManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import javax.annotation.PostConstruct

@Service
class APIListener(
  private val chatManager: ChatManager,
  private val incomingMessageManager: IncomingMessageManager
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostConstruct
  fun listenToOracle() {
    chatManager.addIncomingListener { from, message, chat ->
      log.info("process='OracleAPIListener.listenToOracle' from='${from.asUnescapedString()}' message='received an event'")
      incomingMessageManager.incomingMessage(from, chat, message.body)
    }
  }
}