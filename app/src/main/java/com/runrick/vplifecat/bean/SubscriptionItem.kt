package com.runrick.vplifecat.bean

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/20
 */
data class SubscriptionItem(
    var remarks: String = "",
    var url: String = "",
    var enabled: Boolean = true,
    val addedTime: Long = System.currentTimeMillis()) {
}