package com.kyc3.timestampap.service

import com.kyc3.timestampap.config.properties.ContractsProperties
import com.kyc3.timestampap.service.contracts.Erc20ContractService
import org.slf4j.LoggerFactory
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

    private val log = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun initiateRegistration() {
        log.info("process='RegistrationService' message='requesting exchange keys'")
        oracleService.requestExchangeKeys()
    }

    fun register(account: String) {
        erc20ContractService.allowance(credentials.address, contractsProperties.cashier)
            .thenAccept {
                if ( it <= BigInteger.ZERO) {
                erc20ContractService.mint(credentials.address, BigInteger.valueOf(10000000000000000))
                    .join()
                erc20ContractService.approve(contractsProperties.cashier, BigInteger.valueOf(10000000000000000))
                    .join()
            }
                oracleService.depositRequest(6000)
            }
    }

}
