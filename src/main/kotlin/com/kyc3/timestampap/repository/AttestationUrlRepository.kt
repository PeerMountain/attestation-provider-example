package com.kyc3.timestampap.repository

import com.kyc3.timestampap.repository.entity.AttestationUrlEntity
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
@Transactional(propagation = Propagation.MANDATORY)
class AttestationUrlRepository(
  private val template: R2dbcEntityTemplate,
  private val databaseClient: DatabaseClient
) {

  fun save(entity: AttestationUrlEntity) =
    template.insert(entity)

  fun findNotExpiredToken(token: String, userAddress: String): Mono<AttestationUrlEntity> =
    databaseClient.sql(
      """
      SELECT * FROM attestation_url au
      JOIN user_data ud on ud.id = au.user_id
      WHERE ud.address = :userAddress AND au.token = :token AND au.expiration_time > now()
    """.trimIndent()
    )
      .bind("userAddress", userAddress)
      .bind("token", token)
      .fetch()
      .one()
      .map {
        AttestationUrlEntity(
          id = it["id"] as Long,
          userId = it["user_id"] as Long,
          token = it["token"] as String,
          expirationTime = it["expiration_time"] as LocalDateTime,
        )
      }
}