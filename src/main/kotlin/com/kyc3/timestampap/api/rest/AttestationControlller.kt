package com.kyc3.timestampap.api.rest

import com.kyc3.timestampap.api.rest.model.AttestationDataDto
import com.kyc3.timestampap.repository.entity.AttestationEntity
import com.kyc3.timestampap.service.AttestationService
import com.kyc3.timestampap.service.AttestationUrlService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import javax.validation.Valid
import javax.validation.constraints.Min

@RestController
@RequestMapping("/api/attestation")
class AttestationController(
  private val attestationService: AttestationService,
  private val tokenService: AttestationUrlService
) {

  @PostMapping
  fun attestateData(@Valid @RequestBody request: AttestationDataDto): Mono<AttestationEntity> =
    tokenService.verifyToken(request.token, request.userAddress)
      .flatMap { attestationService.createNewAttestation(request) }

  @GetMapping("{id}")
  fun getAttestationData(@Min(1) @PathVariable("id") id: Long): Mono<AttestationEntity> =
    attestationService.getAttestation(id)

}

