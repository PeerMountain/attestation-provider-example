package com.kyc3.timestampap.service

import com.kyc3.ap.challenge.VerifyChallenge
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class ChallengeVerificationService(
  private val challengeService: ChallengeService,
  private val attestationUrlService: AttestationUrlService
){

  @Transactional
  fun verifyAndGenerateUrl(request: VerifyChallenge.VerifyChallengeRequest): Mono<String> =
    challengeService.verifyChallenge(request)
      .flatMap { attestationUrlService.generate(request.userAddress) }

}