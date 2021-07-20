package com.kyc3.timestampap.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("attestation_url")
data class AttestationUrlEntity(
  @Id
  val id: Long,
  val userId: Long,
  val token: String,
  val expirationTime: LocalDateTime
)
