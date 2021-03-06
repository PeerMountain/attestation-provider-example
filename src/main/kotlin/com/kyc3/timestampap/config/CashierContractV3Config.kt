package com.kyc3.timestampap.config

import com.kyc3.CashierContractV2
import com.kyc3.timestampap.Constants
import com.kyc3.timestampap.config.properties.ContractsProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

@Configuration
class CashierContractV3Config(
    private val contractsProperties: ContractsProperties
) {

    @Bean
    fun cashierContractV3(web3j: Web3j, credentials: Credentials): CashierContractV2 =
        CashierContractV2.load(
            contractsProperties.cashier, web3j, credentials, StaticGasProvider(
                DefaultGasProvider.GAS_PRICE,
                BigInteger.valueOf(680000),
            )
        )
}