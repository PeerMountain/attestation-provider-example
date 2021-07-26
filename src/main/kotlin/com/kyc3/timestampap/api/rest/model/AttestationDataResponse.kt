package com.kyc3.timestampap.api.rest.model

import com.kyc3.timestampap.repository.entity.AttestationEntity

data class AttestationDataResponse(
  val entity: AttestationEntity,
  val redirectUrl: String,
)
