package com.kyc3.timestampap.config

import com.kyc3.CashierContractV3
import com.kyc3.timestampap.Constants
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

@Configuration
class CashierContractV3Config {

    @Bean
    fun cashierContractV3(web3j: Web3j, credentials: Credentials): CashierContractV3 =
        CashierContractV3.load(
            Constants.CASHIER_CONTRACT_ADDRESS, web3j, credentials, StaticGasProvider(
                DefaultGasProvider.GAS_PRICE,
                BigInteger.valueOf(680000),
            )
        )
}