package com.kyc3.timestampap.repository

import com.kyc3.timestampap.repository.entity.ChallengeEntity
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Repository
@Transactional(propagation = Propagation.MANDATORY)
class ChallengeRepository(
  private val template: R2dbcEntityTemplate,
  private val databaseClient: DatabaseClient
) {

  fun updateChallenge(userId: Long, used: Boolean): Mono<Int> =
    template.update(ChallengeEntity::class.java)
      .matching(
        Query.query(
          Criteria.where("user_id").`is`(userId)
        )
      )
      .apply(
        Update.update("used", used)
      )

  fun updateChallenge(userAddress: String, challenge: String, used: Boolean): Mono<Int> =
    databaseClient.sql(
      """UPDATE challenge SET used = :used
          FROM user_data
          WHERE user_data.id = challenge.user_id
          AND user_data.address = :userAddress
          AND challenge.used = false
          AND challenge.challenge = :challenge"""
    )
      .bind("userAddress", userAddress)
      .bind("challenge", challenge)
      .bind("used", used)
      .fetch()
      .rowsUpdated()

  fun createChallenge(challengeEntity: ChallengeEntity): Mono<ChallengeEntity> =
    template.insert(challengeEntity)
}