package com.kyc3.timestampap.service

import com.kyc3.timestampap.model.EncodeAttestationDataRequest
import com.kyc3.timestampap.repository.entity.NftSettingsEntity
import org.springframework.stereotype.Service
import org.web3j.abi.DefaultFunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
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
                Bytes32(Numeric.hexStringToByteArray(request.hashKeyArray)),
                Utf8String(request.tokenUri),
                Bytes32(Numeric.hexStringToByteArray(request.hashedData)),
                hexToByte(Integer.toHexString(request.nftType).padStart(4, '0')),
            )
        )


    fun hexToByte(hexString: String): Bytes2 {
        return Bytes2(
            byteArrayOf(
                byteFromTwoChars(hexString[0], hexString[1]),
                byteFromTwoChars(hexString[2], hexString[3]),
            ),
        )
    }

    fun byteFromTwoChars(a: Char, b: Char): Byte {
        val firstDigit = toDigit(a)
        val secondDigit = toDigit(b)
        return ((firstDigit shl 4) + secondDigit).toByte()
    }

    private fun toDigit(hexChar: Char): Int {
        val digit = Character.digit(hexChar, 16)
        require(digit != -1) { "Invalid Hexadecimal Character: $hexChar" }
        return digit
    }

    fun encodeNftSettings(
        settingsEntity: NftSettingsEntity,
        attestationProvider: String,
        attestationEngine: String
    ): String =
        functionEncoder.encodeParameters(
            listOf(
                Address(attestationProvider),
                Bool(settingsEntity.perpetuity),
                Uint256(settingsEntity.price.toLong()),
                hexToByte(Integer.toHexString(settingsEntity.nftType).padStart(4, '0')),
                Uint256(settingsEntity.expiration),
                Address(attestationEngine),
            )
        )
}