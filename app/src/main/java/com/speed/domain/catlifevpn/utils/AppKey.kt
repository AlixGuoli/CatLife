package com.speed.domain.catlifevpn.utils

import com.speed.domain.catlifevpn.BuildConfig

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/13
 */
interface AppKey {
    companion object {
        const val ANG_PACKAGE = BuildConfig.APPLICATION_ID

        const val KEY_USER_ID = "key_user_id"
        const val TAG_SP = "SP_Config"
        const val IS_FIRST = "IS_FIRST"
        const val DIR_ASSETS = "assets"

        const val PERMISSION_NOTIFICATIONS = "permission_notifications"
        const val KEY_IGNORE_POWER_WHITELIST = "KEY_IGNORE_POWER_WHITELIST"

        const val BROADCAST_ACTION_STATUS = "com.speed.domain.catlifevpn.action.status"
        const val BROADCAST_ACTION_SERVICE = "com.speed.domain.catlifevpn.action.service"
        const val BROADCAST_ACTION_ACTIVITY = "com.speed.domain.catlifevpn.action.activity"

        // Preferences mapped to MMKV
        const val PREF_MODE = "pref_mode"
        const val PREF_SPEED_ENABLED = "pref_speed_enabled"
        const val PREF_SNIFFING_ENABLED = "pref_sniffing_enabled"
        const val PREF_PROXY_SHARING = "pref_proxy_sharing_enabled"
        const val PREF_LOCAL_DNS_ENABLED = "pref_local_dns_enabled"
        const val PREF_FAKE_DNS_ENABLED = "pref_fake_dns_enabled"
        const val PREF_VPN_DNS = "pref_vpn_dns"
        const val PREF_REMOTE_DNS = "pref_remote_dns"
        const val PREF_DOMESTIC_DNS = "pref_domestic_dns"
        const val PREF_LOCAL_DNS_PORT = "pref_local_dns_port"
        const val PREF_ALLOW_INSECURE = "pref_allow_insecure"
        const val PREF_SOCKS_PORT = "pref_socks_port"
        const val PREF_HTTP_PORT = "pref_http_port"
        const val PREF_LOGLEVEL = "pref_core_loglevel"
        const val PREF_LANGUAGE = "pref_language"
        const val PREF_PREFER_IPV6 = "pref_prefer_ipv6"
        const val PREF_ROUTING_DOMAIN_STRATEGY = "pref_routing_domain_strategy"
        const val PREF_ROUTING_MODE = "pref_routing_mode"
        const val PREF_V2RAY_ROUTING_AGENT = "pref_v2ray_routing_agent"
        const val PREF_V2RAY_ROUTING_DIRECT = "pref_v2ray_routing_direct"
        const val PREF_V2RAY_ROUTING_BLOCKED = "pref_v2ray_routing_blocked"
        const val PREF_PER_APP_PROXY = "pref_per_app_proxy"
        const val PREF_PER_APP_PROXY_SET = "pref_per_app_proxy_set"
        const val PREF_BYPASS_APPS = "pref_bypass_apps"
        const val PREF_CONFIRM_REMOVE = "pref_confirm_remove"
        const val PREF_START_SCAN_IMMEDIATE = "pref_start_scan_immediate"

        const val TAG_AGENT = "proxy"
        const val TAG_DIRECT = "direct"
        const val TAG_BLOCKED = "block"

        const val DNS_AGENT = "1.1.1.1"
        const val DNS_DIRECT = "223.5.5.5"

        const val PORT_LOCAL_DNS = "10853"
        const val PORT_SOCKS = "10808"
        const val PORT_HTTP = "10809"

        const val MSG_REGISTER_CLIENT = 1
        const val MSG_STATE_RUNNING = 11
        const val MSG_STATE_NOT_RUNNING = 12
        const val MSG_UNREGISTER_CLIENT = 2
        const val MSG_STATE_START = 3
        const val MSG_STATE_START_SUCCESS = 31
        const val MSG_STATE_START_FAILURE = 32
        const val MSG_STATE_STOP = 4
        const val MSG_STATE_STOP_SUCCESS = 41
        const val MSG_STATE_RESTART = 5
        const val MSG_MEASURE_DELAY = 6
        const val MSG_MEASURE_DELAY_SUCCESS = 61
        const val MSG_MEASURE_CONFIG = 7
        const val MSG_MEASURE_CONFIG_SUCCESS = 71
        const val MSG_MEASURE_CONFIG_CANCEL = 72

        const val STATUS_CONNECT_TIME = 52
        const val STATUS_SPEED = 53
    }
}