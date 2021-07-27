package com.kyc3.timestampap.api.rest.model

data class AttestationDataResponse(
  val entity: AttestationEntityDto,
  val redirectUrl: String,
)
