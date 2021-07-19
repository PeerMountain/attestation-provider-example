package com.kyc3.timestampap.service

import com.kyc3.timestampap.repository.ChallengeRepository
import com.kyc3.timestampap.repository.entity.ChallengeEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.security.SecureRandom

@Service
class ChallengeService(
  private val challengeRepository: ChallengeRepository
) {

  private val secureRandom = SecureRandom()

  @Transactional
  fun generateChallenge(userId: Long): Mono<ChallengeEntity> =
    secureRandom.nextInt().toString()
      .let { challenge ->
        challengeRepository.updateChallenge(userId, false)
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

}