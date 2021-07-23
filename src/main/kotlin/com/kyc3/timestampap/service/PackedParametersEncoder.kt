package com.kyc3.timestampap.service

import org.springframework.stereotype.Component
import org.web3j.abi.TypeEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Bytes
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Numeric

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
      else -> throw IllegalArgumentException("type is not supported")
    }

  private fun encodeBytes(type: Bytes): String =
    type.value.joinToString("") { it.toUByte().toString(16).padStart(2, '0') }

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