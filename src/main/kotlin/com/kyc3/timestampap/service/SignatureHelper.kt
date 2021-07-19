package com.kyc3.timestampap.service

import org.web3j.crypto.Sign
import org.web3j.crypto.Sign.SignatureData


class SignatureHelper {
  companion object {

    fun fromString(signature: String): Sign.SignatureData =
      signature.substring(2).decodeHex()
        .let { signatureBytes ->
          SignatureData(
            signatureBytes[64],
            signatureBytes.copyOfRange(0, 32),
            signatureBytes.copyOfRange(32, 64)
          )
        }

    fun String.decodeHex(): ByteArray {
      require(length % 2 == 0) { "Must have an even length" }

      return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
    }
  }
}