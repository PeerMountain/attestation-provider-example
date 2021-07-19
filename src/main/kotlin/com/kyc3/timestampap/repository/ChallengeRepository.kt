package com.kyc3.timestampap.repository

import com.kyc3.timestampap.repository.entity.ChallengeEntity
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Repository
@Transactional(propagation = Propagation.MANDATORY)
class ChallengeRepository(
  private val template: R2dbcEntityTemplate,
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

  fun createChallenge(challengeEntity: ChallengeEntity): Mono<ChallengeEntity> =
    template.insert(challengeEntity)
}