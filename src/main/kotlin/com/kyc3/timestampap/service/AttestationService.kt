package com.kyc3.timestampap.service

import com.kyc3.timestampap.Constants
import com.kyc3.timestampap.Constants.Companion.ORACLE_ADDRESS
import com.kyc3.timestampap.api.rest.model.AttestationDataDto
import com.kyc3.timestampap.api.rest.model.AttestationDataResponse
import com.kyc3.timestampap.api.rest.model.AttestationEntityDto
import com.kyc3.timestampap.config.properties.OracleUrlProperties
import com.kyc3.timestampap.model.EncodeAttestationDataRequest
import com.kyc3.timestampap.repository.AttestationRepository
import com.kyc3.timestampap.repository.NftSettingsRepository
import com.kyc3.timestampap.repository.UserDataRepository
import com.kyc3.timestampap.repository.entity.AttestationEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.web3j.crypto.Credentials
import org.web3j.crypto.Hash
import org.web3j.crypto.Sign
import org.web3j.utils.Numeric
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class AttestationService(
    private val attestationRepository: AttestationRepository,
    private val abiEncoder: AbiEncoder,
    private val userDataRepository: UserDataRepository,
    private val nftSettingsRepository: NftSettingsRepository,
    private val tokenUriResolver: TokenUriResolver,
    private val credentials: Credentials,
    private val oracleUrlProperties: OracleUrlProperties,
    private val nftMintService: NftMintService
) {

    @Transactional
    fun createNewAttestation(request: AttestationDataDto): Mono<AttestationDataResponse> =
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
            .flatMap { signAttestationData(it, request) }
            .map {
                AttestationDataResponse(
                    AttestationEntityDto(
                        id = it.id,
                        userId = it.userId,
                        attestationData = it.attestationData,
                        attestationTime = it.attestationTime,
                        hashKeyArray = it.hashKeyArray,
                        hashedData = it.hashedData,
                        signature = it.signature,
                        tokenUri = tokenUriResolver.resolveUri(it),
                    ),
                    oracleUrlProperties.baseUrl
                )
            }
            .doOnNext { attestationDataResponse ->
                nftSettingsRepository.findByNftType(request.nftType)
                    .subscribe {
                        nftMintService.nftMint(
                            request.userAddress,
                            0,
                            it,
                            attestationDataResponse.entity
                        )
                    }
            }


    @Transactional
    fun signAttestationData(
        entity: AttestationEntity,
        attestationData: AttestationDataDto
    ): Mono<AttestationEntity> =
        abiEncoder.encodeAttestationData(
            EncodeAttestationDataRequest(
                nftProvider = credentials.address,
                attestationEngine = ORACLE_ADDRESS,
                hashKeyArray = entity.hashKeyArray,
                tokenUri = tokenUriResolver.resolveUri(entity),
                hashedData = entity.hashedData,
            )
        )
            .let {
                Hash.sha3("0x$it")
            }
            .let { Numeric.hexStringToByteArray(it) }
            .let { hashForSign ->
                entity.copy(
                    signature = Sign.signPrefixedMessage(hashForSign, credentials.ecKeyPair)
                        .let { sign -> SignatureHelper.toString(sign) }
                )
            }
            .let { attestationRepository.updateSignature(it) }

    @Transactional
    fun getAttestation(id: Long): Mono<AttestationEntity> =
        attestationRepository.findById(id)
}