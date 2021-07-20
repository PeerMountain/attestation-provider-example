package com.kyc3.timestampap.api.xmpp.router

import com.google.protobuf.Any
import com.kyc3.ap.challenge.ChallengeOuterClass
import com.kyc3.ap.challenge.GenerateChallenge
import com.kyc3.timestampap.service.ChallengeService
import com.kyc3.timestampap.service.UserDataService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GenerateChallengeListener(
  private val generateChallengeService: ChallengeService,
  private val userDataService: UserDataService
) :
  APIListener<GenerateChallenge.GenerateChallengeRequest, GenerateChallenge.GenerateChallengeResponse> {
  override fun type(): Class<GenerateChallenge.GenerateChallengeRequest> =
    GenerateChallenge.GenerateChallengeRequest::class.java

  override fun accept(event: Any, chat: Chat): Mono<GenerateChallenge.GenerateChallengeResponse> =
    Mono.fromSupplier { event.unpack(type()) }
      .flatMap { event ->
        userDataService.createUser(event.userAddress)
          .flatMap { generateChallengeService.generateChallenge(it.id) }
          .map {
            GenerateChallenge.GenerateChallengeResponse.newBuilder()
              .setUserAddress(event.userAddress)
              .setNftType(event.nftType)
              .setChallenge(
                ChallengeOuterClass.Challenge.newBuilder()
                  .setData(it.challenge)
              )
              .build()
          }
      }
}