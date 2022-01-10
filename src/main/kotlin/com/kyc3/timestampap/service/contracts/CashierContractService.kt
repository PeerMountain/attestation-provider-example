package com.kyc3.timestampap.service.contracts

import com.kyc3.CashierContractV2
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger
import java.util.concurrent.CompletableFuture

@Service
class CashierContractService(
    private val cashierContractV2: CashierContractV2
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun requestTreasuryBalance(account: String): CompletableFuture<BigInteger> =
        cashierContractV2.balanceOf(account)
            .sendAsync()
            .also {
                log.info("process=CashierContractV3:requestTreasuryBalance account=$account balance=$it")
            }
}