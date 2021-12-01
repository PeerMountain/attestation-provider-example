package com.kyc3.timestampap.config

import com.kyc3.DummyERC20
import com.kyc3.timestampap.Constants
import com.kyc3.timestampap.config.properties.ContractsProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

@Configuration
class Erc20Config(
    private val contractsProperties: ContractsProperties
) {

    @Bean
    fun erc20(web3j: Web3j, credentials: Credentials): DummyERC20 =
        DummyERC20.load(
            contractsProperties.erc20, web3j, credentials, StaticGasProvider(
                GAS_PRICE,
                BigInteger.valueOf(680000),
            )
        )
}