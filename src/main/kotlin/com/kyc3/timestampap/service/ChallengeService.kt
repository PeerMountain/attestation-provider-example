package com.kyc3.timestampap.service

import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class ChallengeService {

  private val secureRandom = SecureRandom()

  fun generateChallenge(): String =
    secureRandom.nextInt().toString()

}