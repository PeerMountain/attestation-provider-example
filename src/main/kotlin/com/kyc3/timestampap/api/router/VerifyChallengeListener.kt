package com.kyc3.timestampap.api.router

import com.google.protobuf.Any
import com.kyc3.ap.challenge.VerifyChallenge
import com.kyc3.timestampap.service.ChallengeService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class VerifyChallengeListener(
  private val challengeService: ChallengeService
) :
  APIListener<VerifyChallenge.VerifyChallengeRequest, VerifyChallenge.VerifyChallengeResponse> {
  override fun type(): Class<VerifyChallenge.VerifyChallengeRequest> =
    VerifyChallenge.VerifyChallengeRequest::class.java

  override fun accept(event: Any, chat: Chat): Mono<VerifyChallenge.VerifyChallengeResponse> =
    Mono.fromSupplier { event.unpack(type()) }
      .flatMap { challengeService.verifyChallenge(it) }
      .map {
        VerifyChallenge.VerifyChallengeResponse.newBuilder()
          .setRedirectUrl(it)
          .build()
      }
}