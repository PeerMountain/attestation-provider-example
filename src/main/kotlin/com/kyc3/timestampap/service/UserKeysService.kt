package com.kyc3.timestampap.service

import com.kyc3.timestampap.model.UserKeys
import org.ehcache.Cache
import org.springframework.stereotype.Service

@Service
class UserKeysService(
  private val cache: Cache<String, UserKeys>
) {

  fun store(address: String, userKeys: UserKeys): Unit =
    cache.put(address.lowercase(), userKeys)

  fun getUserKeys(address: String): UserKeys? =
    cache.get(address.lowercase())
}