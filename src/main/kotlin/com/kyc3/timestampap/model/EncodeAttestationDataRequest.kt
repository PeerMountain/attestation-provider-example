package com.kyc3.timestampap.model

data class EncodeAttestationDataRequest(
    val nftProvider: String,
    val attestationEngine: String,
    val hashKeyArray: String,
    val tokenUri: String,
    val hashedData: String,
)
