package com.kyc3.timestampap.api.rest

import com.kyc3.timestampap.api.rest.model.ValidationAttestationRequest
import com.kyc3.timestampap.service.AttestationUrlService
import com.kyc3.timestampap.service.Web3JService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.*



@RestController
@RequestMapping("/api/attestation")
class SubmitAttestationController(
  private val web3JService: Web3JService,
  private val attestationUrlService: AttestationUrlService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping("validate")
  fun validateAttestation(
    @RequestBody request: ValidationAttestationRequest
  ) =
    attestationUrlService.verifyToken(request.token, request.userAddress)
      .filter { web3JService.verifySignature(request.challenge, request.signature, request.userAddress) }

}