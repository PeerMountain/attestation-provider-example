package com.kyc3.timestampap.api.rest

import com.kyc3.timestampap.api.rest.model.ValidationAttestationRequest
import com.kyc3.timestampap.repository.entity.AttestationUrlEntity
import com.kyc3.timestampap.service.AttestationUrlService
import com.kyc3.timestampap.service.Web3JService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/api/attestation")
class AttestationVerificationController(
  private val web3JService: Web3JService,
  private val attestationUrlService: AttestationUrlService
) {

  @PostMapping("validate")
  fun validateAttestation(
    @RequestBody request: ValidationAttestationRequest
  ): Mono<AttestationUrlEntity> =
    attestationUrlService.verifyToken(request.token, request.userAddress)
      .filter { web3JService.verifySignature(request.challenge, request.signature, request.userAddress) }

}