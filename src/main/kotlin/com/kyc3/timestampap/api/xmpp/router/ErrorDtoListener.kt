package com.kyc3.timestampap.api.xmpp.router

import com.google.protobuf.Any
import com.kyc3.ErrorDtoOuterClass
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ErrorDtoListener : APIListener<ErrorDtoOuterClass.ErrorDto, ErrorDtoOuterClass.ErrorDto> {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun type(): Class<ErrorDtoOuterClass.ErrorDto> =
    ErrorDtoOuterClass.ErrorDto::class.java

  override fun accept(event: Any, chat: Chat): Mono<ErrorDtoOuterClass.ErrorDto> {
    log.warn("Sender failed to validate signature")
    return Mono.empty()
  }
}