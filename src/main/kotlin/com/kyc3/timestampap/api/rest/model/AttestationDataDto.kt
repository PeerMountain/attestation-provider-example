package com.kyc3.timestampap.api.rest.model

import com.kyc3.timestampap.api.rest.validation.SignatureConstraint

@SignatureConstraint
data class AttestationDataDto(
  val attestationData: String,
  val userAddress: String,
  val challenge: String,
  val signature: String,
  val token: String,
)
