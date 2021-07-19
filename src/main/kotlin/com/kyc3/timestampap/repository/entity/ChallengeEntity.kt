package com.kyc3.timestampap.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("challenge")
data class ChallengeEntity(
  @Id
  val id: Long,
  val userId: Long,
  val challenge: String,
  val used: Boolean
)