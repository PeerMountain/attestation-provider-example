package com.kyc3.timestampap.service

import com.kyc3.timestampap.config.properties.AttestationUrlProperties
import com.kyc3.timestampap.repository.AttestationUrlRepository
import com.kyc3.timestampap.repository.UserDataRepository
import com.kyc3.timestampap.repository.entity.AttestationUrlEntity
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class AttestationUrlService(
  private val attestationUrlRepository: AttestationUrlRepository,
  private val userDataRepository: UserDataRepository,
  private val attestationUrlProperties: AttestationUrlProperties,
) {

  companion object {
    private val TOKEN_LENGTH = 20
  }

  @Transactional
  fun generate(userAddress: String): Mono<String> =
    userDataRepository.findByAddress(userAddress)
      .flatMap { user ->
        RandomStringUtils.random(TOKEN_LENGTH, true, true)
          .let { token ->
            attestationUrlRepository.save(
              AttestationUrlEntity(
                id = 0,
                userId = user.id,
                token = token,
                expirationTime = LocalDateTime.now().plusHours(3)
              )
            )
          }
      }
      .map { "${attestationUrlProperties.baseUrl}?token=${it.token}" }

  @Transactional
  fun verifyToken(token: String, userAddress: String): Mono<AttestationUrlEntity> =
    attestationUrlRepository.findNotExpiredToken(token, userAddress)

}