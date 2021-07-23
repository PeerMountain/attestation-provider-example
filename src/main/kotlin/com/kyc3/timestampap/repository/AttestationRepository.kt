package com.kyc3.timestampap.repository

import com.kyc3.timestampap.repository.entity.AttestationEntity
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Transactional(propagation = Propagation.MANDATORY)
class AttestationRepository(
  private val template: R2dbcEntityTemplate
) {

  fun insert(attestationEntity: AttestationEntity): Mono<AttestationEntity> =
    template.insert(attestationEntity)

  fun updateSignature(attestationEntity: AttestationEntity): Mono<AttestationEntity> =
    template.update(attestationEntity)

  fun findById(attestationId: Long): Mono<AttestationEntity> =
    template.select(AttestationEntity::class.java)
      .matching(Query.query(Criteria.where("id").`is`(attestationId)))
      .one()
}