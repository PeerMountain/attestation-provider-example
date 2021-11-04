package com.kyc3.timestampap.service.contracts

import com.kyc3.ERC20
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger

@Service
class Erc20ContractService(
    private val erc20: ERC20
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun requestBalance(account: String): BigInteger =
        erc20.balanceOf(account)
            .send()
            .also {
                log.info("process=erc20:requestBalance account=$account balance=$it")
            }

    fun mint(account: String, amount: BigInteger): TransactionReceipt =
        erc20.mint(account, amount)
            .send()
            .also {
                log.info("process=erc20:mint account=$account amount=$amount receipt=$it")
            }

    fun allowance(owner: String, spender: String): BigInteger =
        erc20.allowance(owner, spender)
            .send()
            .also {
                log.info("process=erc20:allowance owner=$owner spender=$spender result=$it")
            }

    fun approve(cashierAddress: String, amount: BigInteger): TransactionReceipt =
        erc20.approve(cashierAddress, amount)
            .send()
            .also {
                log.info("process=erc20:mint cashierAddress=$cashierAddress amount=$amount receipt=$it")
            }
}