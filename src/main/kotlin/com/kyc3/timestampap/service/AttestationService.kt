package com.kyc3.timestampap.service

import com.kyc3.timestampap.api.rest.model.AttestationDataDto
import com.kyc3.timestampap.repository.AttestationRepository
import com.kyc3.timestampap.repository.UserDataRepository
import com.kyc3.timestampap.repository.entity.AttestationEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Hash
import org.web3j.crypto.Sign
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class AttestationService(
  private val attestationRepository: AttestationRepository,
  private val userDataRepository: UserDataRepository,
  private val packerParametersEncoder: PackedParametersEncoder,
  private val tokenUriResolver: TokenUriResolver,
  private val ecKeyPair: ECKeyPair
) {

  @Transactional
  fun createNewAttestation(request: AttestationDataDto): Mono<AttestationEntity> =
    userDataRepository.findByAddress(request.userAddress)
      .flatMap {
        attestationRepository.insert(
          AttestationEntity(
            id = 0,
            userId = it.id,
            attestationData = request.attestationData,
            attestationTime = LocalDateTime.now(),
            hashKeyArray = "0x10000006C350000022828531e543c61788be00d3ee000000000735233B600000",
            hashedData = Hash.sha3String(request.attestationData),
            signature = null,
          )
        )
      }
      .flatMap { signAttestationData(it) }

  @Transactional
  fun signAttestationData(entity: AttestationEntity): Mono<AttestationEntity> =
    packerParametersEncoder.encodeParameters(
      listOf(
        Bytes32(entity.hashKeyArray.toByteArray()),
        Utf8String(tokenUriResolver.resolveUri(entity)),
        Bytes32(entity.hashedData.toByteArray())
      )
    )
      .let { Hash.sha3(it) }
      .let { entity.copy(signature = Sign.signMessage(it.toByteArray(), ecKeyPair).toString()) }
      .let { attestationRepository.updateSignature(it) }

  @Transactional
  fun getAttestation(id: Long): Mono<AttestationEntity> =
    attestationRepository.findById(id)
}