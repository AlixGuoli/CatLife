package com.speed.domain.catlifevpn.bean

import com.speed.domain.catlifevpn.utils.AppKey
import com.speed.domain.catlifevpn.utils.Utils

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/20
 */
data class ServerConfig(
    val configVersion: Int = 3,
    val configType: EConfigType,
    var subscriptionId: String = "",
    val addedTime: Long = System.currentTimeMillis(),
    var remarks: String = "",
    val outboundBean: V2rayConfig.OutboundBean? = null,
    var fullConfig: V2rayConfig? = null
) {
    companion object {
        fun create(configType: EConfigType): ServerConfig {
            when(configType) {
                EConfigType.VMESS, EConfigType.VLESS ->
                    return ServerConfig(
                        configType = configType,
                        outboundBean = V2rayConfig.OutboundBean(
                            protocol = configType.name.lowercase(),
                            settings = V2rayConfig.OutboundBean.OutSettingsBean(
                                vnext = listOf(V2rayConfig.OutboundBean.OutSettingsBean.VnextBean(
                                    users = listOf(V2rayConfig.OutboundBean.OutSettingsBean.VnextBean.UsersBean())))),
                            streamSettings = V2rayConfig.OutboundBean.StreamSettingsBean()))
                EConfigType.CUSTOM, EConfigType.WIREGUARD ->
                    return ServerConfig(configType = configType)
                EConfigType.SHADOWSOCKS, EConfigType.SOCKS, EConfigType.TROJAN, EConfigType.HORIZON ->
                    return ServerConfig(
                        configType = configType,
                        outboundBean = V2rayConfig.OutboundBean(
                            protocol = configType.name.lowercase(),
                            settings = V2rayConfig.OutboundBean.OutSettingsBean(
                                servers = listOf(V2rayConfig.OutboundBean.OutSettingsBean.ServersBean())),
                            streamSettings = V2rayConfig.OutboundBean.StreamSettingsBean()))

            }
        }
    }

    fun getProxyOutbound(): V2rayConfig.OutboundBean? {
        if (configType != EConfigType.CUSTOM) {
            return outboundBean
        }
        return fullConfig?.getProxyOutbound()
    }

    fun getAllOutboundTags(): MutableList<String> {
        if (configType != EConfigType.CUSTOM) {
            return mutableListOf(AppKey.TAG_AGENT, AppKey.TAG_DIRECT, AppKey.TAG_BLOCKED)
        }
        fullConfig?.let { config ->
            return config.outbounds.map { it.tag }.toMutableList()
        }
        return mutableListOf()
    }

    fun getV2rayPointDomainAndPort(): String {
        val address = getProxyOutbound()?.getServerAddress().orEmpty()
        val port = getProxyOutbound()?.getServerPort()
        return if (Utils.isIpv6Address(address)) {
            String.format("[%s]:%s", address, port)
        } else {
            String.format("%s:%s", address, port)
        }
    }

    override fun toString(): String {
        return "ServerConfig(configVersion=$configVersion, configType=$configType, subscriptionId='$subscriptionId', addedTime=$addedTime, remarks='$remarks', outboundBean=$outboundBean, fullConfig=$fullConfig)"
    }
}
