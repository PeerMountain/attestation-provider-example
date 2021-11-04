package com.kyc3.timestampap.service

import com.kyc3.oracle.ap.ListNft
import com.kyc3.timestampap.repository.NftSettingsRepository
import com.kyc3.timestampap.repository.entity.NftSettingsEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NftSettingsSyncService(
    private val oracleService: OracleService,
    private val nftSettingsRepository: NftSettingsRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun sync(nftList: ListNft.ListNftResponse) {
        nftSettingsRepository.findAll()
            .collectList()
            .subscribe { entities ->
                val syncState = findSettingsToUpdate(entities, nftList)
                log.info("process=NftSettingsSyncService toInsert=${syncState.toInsert} toRemove=${syncState.toRemove}")
                syncState.toInsert.mapNotNull { nftType -> entities.find { toFind -> toFind.nftType == nftType } }
                    .forEach {
                        oracleService.createNft(it)
                    }
            }
    }

    private fun findSettingsToUpdate(
        entities: MutableList<NftSettingsEntity>,
        oracleState: ListNft.ListNftResponse
    ) = SyncState(
        toInsert = entities.filter { entity ->
            !oracleState.nftSettingsListList.any { oracle ->
                oracle.nft.type == entity.nftType
            }
        }
            .map { it.nftType },
        toRemove = oracleState.nftSettingsListList.filter { oracle ->
            !entities.any { entity ->
                entity.nftType == oracle.nft.type
            }
        }
            .map { it.nft.type }
    )


    data class SyncState(
        val toInsert: List<Int>,
        val toRemove: List<Int>
    )
}