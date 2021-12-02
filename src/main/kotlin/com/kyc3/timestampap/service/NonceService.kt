package com.kyc3.timestampap.service

import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class NonceService {

    fun nextNonce(): Long =
        Random.nextLong(100_000_000)
}