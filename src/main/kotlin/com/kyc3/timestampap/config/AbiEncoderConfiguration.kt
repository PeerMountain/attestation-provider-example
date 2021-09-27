package com.kyc3.timestampap.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.abi.DefaultFunctionEncoder
import org.web3j.abi.FunctionEncoder

@Configuration
class AbiEncoderConfiguration {

    @Bean
    fun functionEncoder(): DefaultFunctionEncoder = DefaultFunctionEncoder()
}