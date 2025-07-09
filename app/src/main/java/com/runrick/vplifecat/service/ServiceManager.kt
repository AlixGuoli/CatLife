package com.runrick.vplifecat.service

import android.content.Context
import android.content.Intent
import android.os.Build
import com.elvishew.xlog.XLog

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/18
 */
object ServiceManager {

    fun startVPMService(context: Context) {
        val intent = Intent(context.applicationContext, V2RayVpnService::class.java)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            XLog.e("startForegroundService")
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

}