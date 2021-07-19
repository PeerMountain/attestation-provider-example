package com.kyc3.timestampap.api.xmpp.router

import com.google.protobuf.Any
import com.google.protobuf.GeneratedMessageV3
import org.jivesoftware.smack.chat2.Chat
import reactor.core.publisher.Mono

interface APIListener<Request : GeneratedMessageV3, Response: GeneratedMessageV3> {
  fun type(): Class<Request>

  fun accept(event: Any, chat: Chat) : Mono<Response>
}