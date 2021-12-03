package com.kyc3.timestampap.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("nft_settings")
data class NftSettingsEntity(
    @Id
    val id: Long,
    val nftType: Int,
    val price: Int,
    val expiration: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
)