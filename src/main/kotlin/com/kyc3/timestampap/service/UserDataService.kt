package com.kyc3.timestampap.service

import com.kyc3.timestampap.repository.UserDataRepository
import com.kyc3.timestampap.repository.entity.UserEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDataService(
  private val userDataRepository: UserDataRepository
) {

  @Transactional
  fun createUser(address: String) =
    userDataRepository.createOrGet(
      UserEntity(
        id = 0,
        address = address
      )
    )
}