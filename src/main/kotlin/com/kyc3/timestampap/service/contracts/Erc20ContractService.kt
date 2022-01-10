package com.kyc3.timestampap.service.contracts

import com.kyc3.DummyERC20
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger
import java.util.concurrent.CompletableFuture

@Service
class Erc20ContractService(
    private val erc20: DummyERC20
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun requestBalance(account: String): CompletableFuture<BigInteger> =
        erc20.balanceOf(account)
            .sendAsync()
            .whenComplete { balance, ex ->
                if (ex == null) {
                    log.info("process=erc20:requestBalance account=$account balance=$balance")
                } else {
                    log.error("process=erc20:requestBalance account=$account", ex)
                }
            }

    fun mint(account: String, amount: BigInteger): CompletableFuture<TransactionReceipt> =
        erc20.mint(account, amount)
            .sendAsync()
            .whenComplete { receipt, ex ->
                if (ex == null) {
                    log.info("process=erc20:mint account=$account amount=$amount receipt=$receipt")
                } else {
                    log.info("process=erc20:mint account=$account amount=$amount", ex)
                }
            }
    fun allowance(owner: String, spender: String): CompletableFuture<BigInteger> =
        erc20.allowance(owner, spender)
            .sendAsync()
            .whenComplete { result, ex ->
                if (ex == null) {
                    log.info("process=erc20:allowance owner=$owner spender=$spender result=$result")
                } else {
                    log.info("process=erc20:allowance owner=$owner spender=$spender", ex)
                }
            }

    fun approve(cashierAddress: String, amount: BigInteger): CompletableFuture<TransactionReceipt> =
        erc20.approve(cashierAddress, amount)
            .sendAsync()
            .whenComplete { receipt, ex ->
                if (ex == null) {
                    log.info("process=erc20:approve cashierAddress=$cashierAddress amount=$amount receipt=$receipt")
                } else {
                    log.info("process=erc20:approve cashierAddress=$cashierAddress amount=$amount", ex)
                }
            }
}