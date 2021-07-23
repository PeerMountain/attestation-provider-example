package com.kyc3.timestampap.service

import org.springframework.stereotype.Component
import org.web3j.abi.TypeEncoder
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Numeric
import java.nio.charset.StandardCharsets

@Component
class PackedParametersEncoder {

  fun encodeParameters(parameters: List<Type<out Any>>): String =
    parameters.joinToString("") { resolveType(it) }

  fun resolveType(type: Type<out Any>): String =
    when (type) {
      is Bytes -> encodeBytes(type)
      is Bool -> encodeBool(type)
      is Uint256 -> TypeEncoder.encode(type)
      is Address -> encodeAddress(type)
      is Utf8String -> encodeBytes(type.value.toByteArray(StandardCharsets.UTF_8))
      else -> throw IllegalArgumentException("type: ${type.javaClass.simpleName} is not supported")
    }

  private fun encodeBytes(type: Bytes): String =
    encodeBytes(type.value)

  private fun encodeBytes(bytes: ByteArray) =
    bytes.joinToString("") { it.toUByte().toString(16).padStart(2, '0') }

  private fun encodeBool(type: Bool): String =
    ByteArray(1)
      .also {
        if (type.value) {
          it[0] = 1
        }
      }
      .let { Numeric.toHexStringNoPrefix(it) }

  fun encodeAddress(address: Address): String =
    address.value.replace("0x", "")

}