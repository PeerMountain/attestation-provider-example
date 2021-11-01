package com.kyc3.timestampap.repository

import com.kyc3.timestampap.repository.entity.NftSettingsEntity
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class NftSettingsRepository(
    private val template: R2dbcEntityTemplate,
    private val databaseClient: DatabaseClient
) {

    fun findAll() = template.select(NftSettingsEntity::class.java)
        .all()
}