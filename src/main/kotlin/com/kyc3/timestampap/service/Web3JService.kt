package com.kyc3.timestampap.service

import org.springframework.stereotype.Service
import org.web3j.crypto.Hash
import org.web3j.crypto.Keys
import org.web3j.crypto.Sign
import org.web3j.utils.Numeric

@Service
class Web3JService {

  fun verifySignature(challenge: String, signature: String, address: String): Boolean =
    SignatureHelper.fromString(signature)
      .let { signatureData ->
        Numeric.hexStringToByteArray(Hash.sha3String(challenge))
          .let { data ->
            Sign.signedPrefixedMessageToKey(data, signatureData)
          }
      }
      .let { Keys.getAddress(it) }
      .let { address.contains(it) }
}