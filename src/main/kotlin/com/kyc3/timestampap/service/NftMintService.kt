package com.kyc3.timestampap.service

import com.kyc3.timestampap.api.rest.model.AttestationEntityDto
import com.kyc3.timestampap.config.properties.OracleProperties
import com.kyc3.timestampap.model.EncodeAttestationDataRequest
import com.kyc3.timestampap.repository.entity.NftSettingsEntity
import org.springframework.stereotype.Service
import org.web3j.crypto.Credentials

@Service
class NftMintService(
    private val abiEncoder: AbiEncoder,
    private val credentials: Credentials,
    private val web3JService: Web3JService,
    private val uriResolver: TokenUriResolver,
    private val oracleService: OracleService,
    private val oracleProperties: OracleProperties,
) {

    fun nftMint(
        userAddress: String,
        nonce: Long,
        nftSettings: NftSettingsEntity,
        attestationEntity: AttestationEntityDto
    ) {
        val encodedNftSettings = abiEncoder.encodeNftSettings(nftSettings, credentials.address)
        val nftSigned = encodedNftSettings.let { web3JService.signHex(it) }
            .let { SignatureHelper.toString(it) }

        val encodeAttestationData = abiEncoder.encodeAttestationData(
            EncodeAttestationDataRequest(
                credentials.address,
                oracleProperties.address,
                attestationEntity.hashKeyArray,
                uriResolver.resolveUri(attestationEntity),
                attestationEntity.hashedData,
            )
        )

        val encodedNftMint = abiEncoder.encodeNftMint(
            userAddress,
            nonce,
            encodedNftSettings,
            nftSigned,
            encodeAttestationData,
            attestationEntity.signature ?: throw IllegalStateException()
        )

        val encodedNftMintSigned = web3JService.signHex(encodedNftMint)

        oracleService.nftMint(
            encodedNftMint,
            SignatureHelper.toString(encodedNftMintSigned)
        )
    }

}