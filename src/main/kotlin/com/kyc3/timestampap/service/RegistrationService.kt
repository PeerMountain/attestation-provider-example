package com.kyc3.timestampap.service

import com.kyc3.timestampap.config.properties.ContractsProperties
import com.kyc3.timestampap.service.contracts.Erc20ContractService
import org.springframework.stereotype.Service
import org.web3j.crypto.Credentials
import java.math.BigInteger
import javax.annotation.PostConstruct

@Service
class RegistrationService(
    private val oracleService: OracleService,
    private val credentials: Credentials,
    private val contractsProperties: ContractsProperties,
    private val erc20ContractService: Erc20ContractService,
) {

    @PostConstruct
    fun initiateRegistration() {
        oracleService.requestExchangeKeys()
    }

    fun register(account: String) {
        if (erc20ContractService.allowance(credentials.address, contractsProperties.cashier) <= BigInteger.ZERO) {
            erc20ContractService.mint(credentials.address, BigInteger.valueOf(10000000000000000))
            erc20ContractService.approve(contractsProperties.cashier, BigInteger.valueOf(10000000000000000))
        }
        oracleService.depositRequest(6000)
    }

}
