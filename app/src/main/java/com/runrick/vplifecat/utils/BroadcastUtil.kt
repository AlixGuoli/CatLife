package com.runrick.vplifecat.utils

import android.content.Context
import android.content.Intent

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/18
 */
object BroadcastUtil {

    fun sendSpeed(context: Context, what: Int, download: Long, upload: Long) {
        try {
            val intent = Intent()
            intent.action = AppKey.BROADCAST_ACTION_STATUS
            intent.putExtra("key", what)
            intent.putExtra("download", download)
            intent.putExtra("upload", upload)
            context.sendBroadcast(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendTime(context: Context, what: Int, timeStr: String) {
        try {
            val intent = Intent()
            intent.action = AppKey.BROADCAST_ACTION_STATUS
            intent.putExtra("key", what)
            intent.putExtra("time", timeStr)
            context.sendBroadcast(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}