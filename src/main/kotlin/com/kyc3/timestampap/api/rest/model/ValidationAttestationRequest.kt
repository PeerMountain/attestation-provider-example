package com.kyc3.timestampap.api.rest.model

data class ValidationAttestationRequest(
  val token: String,
  val challenge: String,
  val signature: String,
  val userAddress: String,
)