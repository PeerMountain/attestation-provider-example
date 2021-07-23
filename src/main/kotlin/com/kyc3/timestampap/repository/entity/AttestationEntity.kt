package com.kyc3.timestampap.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("attestation")
data class AttestationEntity(
  @Id
  val id: Long,
  val userId: Long,
  val attestationData: String,
  val attestationTime: LocalDateTime,
  val hashKeyArray: String,
  val hashedData: String,
  val signature: String?
)