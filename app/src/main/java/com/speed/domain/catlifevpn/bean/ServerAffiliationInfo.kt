package com.speed.domain.catlifevpn.bean

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/20
 */
data class ServerAffiliationInfo(var testDelayMillis: Long = 0L) {
    fun getTestDelayString(): String {
        if (testDelayMillis == 0L) {
            return ""
        }
        return testDelayMillis.toString() + "ms"
    }
}