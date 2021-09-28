package com.kyc3.timestampap.service

import org.springframework.stereotype.Service
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Hash
import org.web3j.crypto.Keys
import org.web3j.crypto.Sign
import org.web3j.protocol.Web3j
import org.web3j.utils.Numeric

@Service
class Web3JService(
  private val web3j: Web3j,
  private val ecKeyPair: ECKeyPair
) {

  fun verifySignature(challenge: String, signature: String, address: String): Boolean =
    SignatureHelper.fromString(signature)
      .let { signatureData ->
        Numeric.hexStringToByteArray(Hash.sha3String(challenge))
          .let { data ->
            Sign.signedPrefixedMessageToKey(data, signatureData)
          }
      }
      .let { Keys.getAddress(it) }
      .let { address.contains(it, true) }

  fun sign(body: String): Sign.SignatureData =
    Sign.signPrefixedMessage(Numeric.hexStringToByteArray(Hash.sha3String(body)), ecKeyPair)

}