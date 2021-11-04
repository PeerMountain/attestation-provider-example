package com.kyc3.timestampap.api.xmpp.router

import com.google.protobuf.Any
import com.kyc3.oracle.ap.CreateNft
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateNftListener: APIListener<CreateNft.CreateNftResponse, CreateNft.CreateNftResponse> {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun type(): Class<CreateNft.CreateNftResponse> = CreateNft.CreateNftResponse::class.java

    override fun accept(event: Any, chat: Chat): Mono<CreateNft.CreateNftResponse> {
        log.info("process=CreateNftListener message='nft created'")
        return Mono.empty()
    }
}