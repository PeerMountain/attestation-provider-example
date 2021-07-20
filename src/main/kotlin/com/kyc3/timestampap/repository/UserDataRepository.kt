package com.kyc3.timestampap.repository

import com.kyc3.timestampap.repository.entity.UserEntity
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Repository
@Transactional(propagation = Propagation.MANDATORY)
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

  fun findByAddress(userAddress: String): Mono<UserEntity> =
    template.selectOne(
      Query.query(
        Criteria.where("address").`is`(userAddress)
      ),
      UserEntity::class.java
    )
}