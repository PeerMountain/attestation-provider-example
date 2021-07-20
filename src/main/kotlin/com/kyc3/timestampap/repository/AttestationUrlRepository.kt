package com.kyc3.timestampap.repository

import com.kyc3.timestampap.repository.entity.AttestationUrlEntity
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(propagation = Propagation.MANDATORY)
class AttestationUrlRepository(
  private val template: R2dbcEntityTemplate
) {

  fun save(entity: AttestationUrlEntity) =
    template.insert(entity)
}