package com.kyc3.timestampap.api.xmpp.router

import com.google.protobuf.Any
import com.kyc3.oracle.ap.ListNft
import com.kyc3.timestampap.service.NftSettingsSyncService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ListNftListener(
    private val nftSettingsSyncService: NftSettingsSyncService
) : APIListener<ListNft.ListNftResponse, ListNft.ListNftResponse> {
    override fun type(): Class<ListNft.ListNftResponse> =
        ListNft.ListNftResponse::class.java

    override fun accept(event: Any, chat: Chat): Mono<ListNft.ListNftResponse> =
        event.unpack(type())
            .let {
                nftSettingsSyncService.sync(it)
                Mono.empty()
            }
}