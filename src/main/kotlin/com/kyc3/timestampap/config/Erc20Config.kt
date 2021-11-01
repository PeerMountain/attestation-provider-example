package com.kyc3.timestampap.config

import com.kyc3.ERC20
import com.kyc3.timestampap.Constants
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

@Configuration
class Erc20Config {

    @Bean
    fun erc20(web3j: Web3j, credentials: Credentials): ERC20 =
        ERC20.load(
            Constants.ERC20_CONTRACT_ADDRESS, web3j, credentials, StaticGasProvider(
                GAS_PRICE,
                BigInteger.valueOf(680000),
            )
        )
}