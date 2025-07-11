package com.speed.domain.catlifevpn.bean

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/20
 */
enum class EConfigType (val value: Int, val protocolScheme: String){
    VMESS(1, "vmess://"),
    CUSTOM(2, ""),
    SHADOWSOCKS(3, "ss://"),
    SOCKS(4, "socks://"),
    VLESS(5, "vless://"),
    TROJAN(6, "trojan://"),
    WIREGUARD(7, "wireguard://"),
    HORIZON(8, "horizon://");

    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.value == value }
    }
}