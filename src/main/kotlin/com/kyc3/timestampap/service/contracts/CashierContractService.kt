package com.kyc3.timestampap.service.contracts

import com.kyc3.CashierContractV3
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger
import kotlin.math.abs

@Service
class CashierContractService(
    private val cashierContractV3: CashierContractV3
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun requestTreasuryBalance(account: String): BigInteger =
        cashierContractV3.balanceOf(account)
            .send()
            .also {
                log.info("process=CashierContractV3:requestTreasuryBalance account=$account balance=$it")
            }

    fun depositTreasuryAccount(amount: BigInteger): TransactionReceipt =
        cashierContractV3.deposit(amount)
            .send()
            .also {
                log.info("process=CashierContractV3:depositTreasuryAccount amount=$amount receipt=$it")
            }
}