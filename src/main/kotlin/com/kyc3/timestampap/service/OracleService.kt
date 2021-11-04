package com.kyc3.timestampap.service

import com.kyc3.ERC20
import com.kyc3.Message
import com.kyc3.oracle.ap.AttestationProviderOuterClass
import com.kyc3.oracle.ap.CreateNft
import com.kyc3.oracle.ap.Data
import com.kyc3.oracle.ap.ListNft
import com.kyc3.oracle.ap.Register
import com.kyc3.oracle.nft.Nft
import com.kyc3.timestampap.Constants
import com.kyc3.timestampap.Constants.Companion.ERC20_CONTRACT_ADDRESS
import com.kyc3.timestampap.Constants.Companion.ORACLE_ADDRESS
import com.kyc3.timestampap.api.xmpp.OracleAPIResponse
import com.kyc3.timestampap.repository.entity.NftSettingsEntity
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import org.springframework.stereotype.Service
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.gas.DefaultGasProvider

@Service
class OracleService(
    chatManager: ChatManager,
    private val apiResponse: OracleAPIResponse,
    private val userKeysService: UserKeysService,
    private val credentials: Credentials,
    private val exchangeKeysHolder: ExchangeKeysHolder,
    private val web3JService: Web3JService,
    private val abiEncoder: AbiEncoder
) {

    private val oracleJid: EntityBareJid = JidCreate.entityBareFrom(
        "$ORACLE_ADDRESS@xmpp.kyc3.com"
    )

    private val oracleChat: Chat = chatManager.chatWith(oracleJid)

    fun requestAttestationProviderData() {
        val userKeys = userKeysService.getUserKeys(
            "$ORACLE_ADDRESS@xmpp.kyc3.com"
        )

        if (userKeys != null) {
            apiResponse.responseToClient(
                oracleChat,
                Data.AttestationProviderDataRequest.newBuilder()
                    .setAddress(credentials.address)
                    .build()
            )
        }
    }

    fun sendRegistration(receipt: TransactionReceipt) {
        Register.RegisterAttestationProviderRequest.newBuilder()
            .setProvider(
                AttestationProviderOuterClass.AttestationProvider.newBuilder()
                    .setAddress(credentials.address)
                    .setName(credentials.address)
                    .setInitialTransaction(receipt.transactionHash)
                    .build()
            )
            .build()
            .let {
                apiResponse.responseToClient(
                    oracleChat,
                    it
                )
            }
    }

    fun requestExchangeKeys() {
        apiResponse.responseDirectly(
            oracleChat,
            Message.GeneralMessage.newBuilder()
                .setExchange(exchangeKeysHolder.generateExchangeMessageRequest())
                .build()
        )
    }

    fun requestNftSettingsList() {
        ListNft.ListNftRequest.newBuilder()
            .setApAddress(credentials.address)
            .build()
            .let {
                apiResponse.responseToClient(
                    oracleChat,
                    it
                )
            }
    }

    fun createNft(settings: NftSettingsEntity) {
        abiEncoder.encodeNftSettings(settings, credentials.address, ORACLE_ADDRESS)
            .let { web3JService.sign(it) }
            .let { SignatureHelper.toString(it) }
            .let {
                CreateNft.CreateNftRequest.newBuilder()
                    .setNftSettings(
                        Nft.NftSettings.newBuilder()
                            .setType(settings.nftType)
                            .setPerpetuity(settings.perpetuity)
                            .setPrice(settings.price)
                            .setExpiration(settings.expiration)
                            .setAttestationProvider(credentials.address)
                            .setAttestationEngine(ORACLE_ADDRESS)
                            .setSignedMessage(it)
                    )
                    .build()
            }
            .let { apiResponse.responseToClient(oracleChat, it) }
    }
}