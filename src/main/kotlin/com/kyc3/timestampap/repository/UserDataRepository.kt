package com.kyc3.timestampap.repository

import com.kyc3.timestampap.repository.entity.UserEntity
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class UserDataRepository(
  private val template: R2dbcEntityTemplate,
  private val databaseClient: DatabaseClient
) {

  fun createOrGet(userEntity: UserEntity): Mono<UserEntity> =
    databaseClient.sql(
      "INSERT INTO user_data (address) " +
          "VALUES (:address) " +
          "ON CONFLICT(address) DO UPDATE SET address = excluded.address " +
          "RETURNING *"
    )
      .bind("address", userEntity.address)
      .fetch()
      .one()
      .map {
        UserEntity(
          id = it["id"] as Long,
          address = it["address"] as String
        )
      }

}