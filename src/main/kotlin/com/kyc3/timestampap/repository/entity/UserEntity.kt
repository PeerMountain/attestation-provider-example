package com.kyc3.timestampap.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("user_data")
data class UserEntity(
  @Id
  val id: Long,
  val address: String
)
