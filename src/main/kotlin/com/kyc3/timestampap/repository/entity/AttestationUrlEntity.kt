package com.kyc3.timestampap.repository.entity

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class AttestationUrlEntity(
  @Id
  val id: Long,
  val userId: Long,
  val token: String,
  val expirationTime: LocalDateTime
)
