package com.speed.domain.catlifevpn.service

import android.app.Service

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/19
 */
interface ServiceControl {
    fun getService(): Service

    fun startService()

    fun stopService()

    fun vpnProtect(socket: Int): Boolean
}