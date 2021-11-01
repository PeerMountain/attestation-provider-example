package com.kyc3.timestampap.service

import com.kyc3.timestampap.Constants.Companion.CASHIER_CONTRACT_ADDRESS
import com.kyc3.timestampap.service.contracts.CashierContractService
import com.kyc3.timestampap.service.contracts.Erc20ContractService
import org.springframework.stereotype.Service
import org.web3j.crypto.Credentials
import java.math.BigInteger
import javax.annotation.PostConstruct

@Service
class RegistrationService(
    private val oracleService: OracleService,
    private val credentials: Credentials,
    private val cashierContractService: CashierContractService,
    private val erc20ContractService: Erc20ContractService,
) {

    @PostConstruct
    fun initiateRegistration() {
        oracleService.requestExchangeKeys()
    }

    fun register(account: String) {
        if (erc20ContractService.requestBalance(account) <= BigInteger.ZERO) {
            erc20ContractService.mint(account, BigInteger.valueOf(10000000000000000))
        }
        erc20ContractService.allowance(credentials.address, CASHIER_CONTRACT_ADDRESS)
        if (cashierContractService.requestTreasuryBalance(account) <= BigInteger.ZERO) {
            erc20ContractService.approve(CASHIER_CONTRACT_ADDRESS, BigInteger.valueOf(1000000))
        }
        val receipt = cashierContractService.depositTreasuryAccount(BigInteger.valueOf(6000))
        oracleService.sendRegistration(receipt)
    }

}
