package com.kyc3.timestampap.service

import com.kyc3.Message
import com.kyc3.oracle.ap.CreateNft
import com.kyc3.oracle.ap.Data
import com.kyc3.oracle.ap.ListNft
import com.kyc3.oracle.nft.Nft
import com.kyc3.oracle.user.Deposit
import com.kyc3.oracle.user.NftMint
import com.kyc3.timestampap.api.xmpp.OracleAPIResponse
import com.kyc3.timestampap.config.properties.ContractsProperties
import com.kyc3.timestampap.config.properties.OracleProperties
import com.kyc3.timestampap.repository.entity.NftSettingsEntity
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import org.springframework.stereotype.Service
import org.web3j.crypto.Credentials

@Service
class OracleService(
    chatManager: ChatManager,
    private val apiResponse: OracleAPIResponse,
    private val userKeysService: UserKeysService,
    private val credentials: Credentials,
    private val exchangeKeysHolder: ExchangeKeysHolder,
    private val web3JService: Web3JService,
    private val abiEncoder: AbiEncoder,
    private val contractsProperties: ContractsProperties,
    private val oracleProperties: OracleProperties,
    private val nonceService: NonceService,
) {

    private val oracleJid: EntityBareJid = JidCreate.entityBareFrom(
        "${oracleProperties.address}@xmpp.kyc3.com"
    )

    private val oracleChat: Chat = chatManager.chatWith(oracleJid)

    fun requestAttestationProviderData() {
        val userKeys = userKeysService.getUserKeys(
            "${oracleProperties.address}@xmpp.kyc3.com"
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
        abiEncoder.encodeNftSettings(settings, credentials.address)
            .let { web3JService.signHex(it) }
            .let { SignatureHelper.toString(it) }
            .let {
                CreateNft.CreateNftRequest.newBuilder()
                    .setNftSettings(
                        Nft.NftSettings.newBuilder()
                            .setType(settings.nftType)
                            .setPrice(settings.price)
                            .setExpiration(settings.expiration)
                            .setAttestationProvider(credentials.address)
                            .setSignedMessage(it)
                    )
                    .build()
            }
            .let { apiResponse.responseToClient(oracleChat, it) }
    }

    fun depositRequest(depositAmount: Long) =
        abiEncoder.encodeDeposit(
            depositAmount,
            nonceService.nextNonce(),
            contractsProperties.cashier
        )
            .let { encoded ->
                encoded to web3JService.signHex(encoded)
                    .let { signature -> SignatureHelper.toString(signature) }
            }
            .let { (message, signature) ->
                Deposit.DepositRequest.newBuilder()
                    .setMessage("0x$message")
                    .setSignature(signature)
                    .build()
            }
            .let {
                apiResponse.responseToClient(oracleChat, it)
            }

    fun nftMint(
        message: String,
        signature: String
    ) =
        NftMint.NftMintRequest.newBuilder()
            .setMessage(message)
            .setSignature(signature)
            .build()
            .let {
                apiResponse.responseToClient(oracleChat, it)
            }
}