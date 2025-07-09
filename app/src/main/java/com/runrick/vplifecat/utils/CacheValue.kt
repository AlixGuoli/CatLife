package com.runrick.vplifecat.utils

import androidx.lifecycle.MutableLiveData

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/13
 */
class CacheValue {
    companion object {
        var isFirst by SPUtil(AppKey.IS_FIRST, true)

        var isRunning = MutableLiveData(false)

        var PERMISSION_NOTIFICATIONS by SPUtil(AppKey.PERMISSION_NOTIFICATIONS, false)
        var KEY_IGNORE_POWER_WHITELIST by SPUtil(AppKey.KEY_IGNORE_POWER_WHITELIST, false)
    }
}