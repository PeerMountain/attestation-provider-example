package com.kyc3.timestampap.api.rest.model

import com.kyc3.timestampap.model.EncodeAttestationDataRequest
import java.time.LocalDateTime

data class AttestationEntityDto(
  val id: Long,
  val userId: Long,
  val attestationData: String,
  val attestationTime: LocalDateTime,
  val hashKeyArray: String,
  val hashedData: String,
  val signature: String?,
  val tokenUri: String,
  val tempPrivKey: String,
  val tempEncode: String,
  val tempEncodeHash: String,
  val request: EncodeAttestationDataRequest,
)