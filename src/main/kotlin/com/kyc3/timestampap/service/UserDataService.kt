package com.kyc3.timestampap.service

import com.kyc3.timestampap.repository.UserDataRepository
import com.kyc3.timestampap.repository.entity.UserEntity
import org.springframework.stereotype.Service

@Service
class UserDataService(
  private val userDataRepository: UserDataRepository
) {

  fun createUser(address: String) =
    userDataRepository.createOrGet(
      UserEntity(
        id = 0,
        address = address
      )
    )
}