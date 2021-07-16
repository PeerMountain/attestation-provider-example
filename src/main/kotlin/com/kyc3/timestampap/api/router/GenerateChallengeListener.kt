package com.kyc3.timestampap.api.router

import com.google.protobuf.Any
import com.kyc3.ap.challenge.ChallengeOuterClass
import com.kyc3.ap.challenge.GenerateChallenge
import com.kyc3.timestampap.service.ChallengeService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class GenerateChallengeListener(
  private val generateChallengeService: ChallengeService
) :
  APIListener<GenerateChallenge.GenerateChallengeRequest, GenerateChallenge.GenerateChallengeResponse> {
  override fun type(): Class<GenerateChallenge.GenerateChallengeRequest> =
    GenerateChallenge.GenerateChallengeRequest::class.java

  override fun accept(event: Any, chat: Chat): GenerateChallenge.GenerateChallengeResponse =
    GenerateChallenge.GenerateChallengeResponse.newBuilder()
      .setChallenge(ChallengeOuterClass.Challenge.newBuilder()
        .setData(generateChallengeService.generateChallenge()))
      .build()
}