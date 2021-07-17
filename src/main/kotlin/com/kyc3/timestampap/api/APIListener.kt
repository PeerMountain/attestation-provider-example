package com.kyc3.timestampap.api

import com.kyc3.timestampap.api.router.APIRouter
import org.jivesoftware.smack.chat2.ChatManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class APIListener(
  private val chatManager: ChatManager,
  private val oracleRouter: APIRouter,
  private val messageParser: MessageParser,
  private val oracleApiResponse: OracleAPIResponse
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostConstruct
  fun listenToOracle() {
    chatManager.addIncomingListener { from, message, chat ->
      log.info("process='APIListener.listenToOracle' from='${from.asUnescapedString()}' message='received an event'")
      messageParser.parseMessage(message)
        .let { oracleRouter.route(it, chat) }
        ?.let { oracleApiResponse.responseToClient(chat, it) }
    }
  }
}