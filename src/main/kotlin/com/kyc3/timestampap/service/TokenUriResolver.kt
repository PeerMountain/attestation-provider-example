package com.kyc3.timestampap.service

import com.kyc3.timestampap.api.rest.model.AttestationEntityDto
import com.kyc3.timestampap.config.properties.AttestationUrlProperties
import com.kyc3.timestampap.repository.entity.AttestationEntity
import org.springframework.stereotype.Service

@Service
class TokenUriResolver(
  private val attestationUrlProperties: AttestationUrlProperties
) {

  fun resolveUri(attestationEntity: AttestationEntity): String =
    "${attestationUrlProperties.baseUrl}/${attestationEntity.id}"

  fun resolveUri(attestationEntity: AttestationEntityDto): String =
    "${attestationUrlProperties.baseUrl}/${attestationEntity.id}"

}