package com.kyc3.timestampap.service

import com.kyc3.ap.challenge.VerifyChallenge
import com.kyc3.timestampap.repository.ChallengeRepository
import com.kyc3.timestampap.repository.entity.ChallengeEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.lang.RuntimeException
import java.security.SecureRandom

@Service
class ChallengeService(
  private val challengeRepository: ChallengeRepository,
  private val web3JService: Web3JService
) {

  private val secureRandom = SecureRandom()

  @Transactional
  fun generateChallenge(userId: Long): Mono<ChallengeEntity> =
    secureRandom.nextInt().toString()
      .let { challenge ->
        challengeRepository.updateChallenge(userId, true)
          .flatMap {
            challengeRepository.createChallenge(
              ChallengeEntity(
                id = 0,
                userId = userId,
                challenge = challenge,
                used = false
              )
            )
          }
      }

  fun useCurrentChallenge(userAddress: String, challenge: String): Mono<Int> =
    challengeRepository.updateChallenge(userAddress, challenge, true)


  @Transactional
  fun verifyChallenge(request: VerifyChallenge.VerifyChallengeRequest): Mono<Int> =
    web3JService.verifySignature(request.challenge, request.signedChallenge, request.userAddress)
      .takeIf { it }
      .let { Mono.justOrEmpty(it) }
      .flatMap { useCurrentChallenge(request.userAddress, request.challenge) }
      .switchIfEmpty(Mono.error(RuntimeException("Signature verification error")))

}