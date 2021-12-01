package com.kyc3.timestampap.service

import com.kyc3.timestampap.model.EncodeAttestationDataRequest
import com.kyc3.timestampap.repository.entity.NftSettingsEntity
import org.springframework.stereotype.Service
import org.web3j.abi.DefaultFunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.DynamicBytes
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Bytes2
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Numeric

@Service
class AbiEncoder(
    private val functionEncoder: DefaultFunctionEncoder
) {
    fun encodeAttestationData(request: EncodeAttestationDataRequest): String =
        functionEncoder.encodeParameters(
            listOf(
                Address(request.nftProvider),
                Address(request.attestationEngine),
                Bytes32(Numeric.hexStringToByteArray(request.hashKeyArray)),
                Utf8String(request.tokenUri),
                Bytes32(Numeric.hexStringToByteArray(request.hashedData)),
            )
        )

    fun encodeNftSettings(
        settingsEntity: NftSettingsEntity,
        attestationProvider: String,
    ): String =
        functionEncoder.encodeParameters(
            listOf(
                Address(attestationProvider),
                Uint256(settingsEntity.price.toLong()),
                hexToByte(Integer.toHexString(settingsEntity.nftType).padStart(4, '0')),
                Uint256(settingsEntity.expiration),
            )
        )

    fun encodeDeposit(
        depositAmount: Long,
        nonce: Long,
        cashierAddress: String
    ): String =
        functionEncoder.encodeParameters(
            listOf(
                Uint256(depositAmount),
                Uint256(nonce),
                Address(cashierAddress)
            )
        )

    fun encodeNftMint(
        userAddress: String,
        nonce: Long,
        encodedNftSettings: String,
        encodedNftSigned: String,
        encodedAttestationData: String,
        encodedAttestationDataSigned: String,
    ): String =
        functionEncoder.encodeParameters(
            listOf(
                Address(userAddress),
                Uint256(nonce),
                DynamicBytes(Numeric.hexStringToByteArray(encodedNftSettings)),
                DynamicBytes(Numeric.hexStringToByteArray(encodedNftSigned)),
                DynamicBytes(Numeric.hexStringToByteArray(encodedAttestationData)),
                DynamicBytes(Numeric.hexStringToByteArray(encodedAttestationDataSigned)),
            )
        )


    private fun hexToByte(hexString: String): Bytes2 {
        return Bytes2(
            byteArrayOf(
                byteFromTwoChars(hexString[0], hexString[1]),
                byteFromTwoChars(hexString[2], hexString[3]),
            ),
        )
    }

    private fun byteFromTwoChars(a: Char, b: Char): Byte {
        val firstDigit = toDigit(a)
        val secondDigit = toDigit(b)
        return ((firstDigit shl 4) + secondDigit).toByte()
    }

    private fun toDigit(hexChar: Char): Int {
        val digit = Character.digit(hexChar, 16)
        require(digit != -1) { "Invalid Hexadecimal Character: $hexChar" }
        return digit
    }
}