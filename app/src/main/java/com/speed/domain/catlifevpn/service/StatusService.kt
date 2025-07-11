package com.speed.domain.catlifevpn.service

import android.app.Service
import android.content.Intent
import android.net.TrafficStats.getTotalRxBytes
import android.net.TrafficStats.getTotalTxBytes
import android.os.IBinder
import com.speed.domain.catlifevpn.utils.AppKey
import com.speed.domain.catlifevpn.utils.BroadcastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit


/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/18
 */
class StatusService: Service() {

    private var lastTotalRxBytes = 0L
    private var lastTotalTxBytes = 0L
    private var lastTimeStamp = System.currentTimeMillis()
    private var timer: Timer? = null
    private lateinit var job: Job

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {
        super.onCreate()
        startTimer()
        lastTotalRxBytes = getTotalRxBytes()
        lastTotalTxBytes = getTotalTxBytes()
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                delay(1500)
                val nowTotalRxBytes = getTotalRxBytes()
                val nowTotalTxBytes = getTotalTxBytes()
                val nowTimeStamp = System.currentTimeMillis()
                val speedRx = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp)).toDouble()
                val speedTx = ((nowTotalTxBytes - lastTotalTxBytes) * 1000 / (nowTimeStamp - lastTimeStamp)).toDouble()
                lastTimeStamp = nowTimeStamp
                lastTotalRxBytes = nowTotalRxBytes
                lastTotalTxBytes = nowTotalTxBytes
//                val speedDownload = formatBytes(speedRx.toLong())
//                val speedUpload = formatBytes(speedTx.toLong())
                val speedDownload = speedRx.toLong()
                val speedUpload = speedTx.toLong()
//                XLog.e("Download speed: $speedDownload")
//                XLog.e("Upload speed: $speedUpload")
                BroadcastUtil.sendSpeed(this@StatusService, AppKey.STATUS_SPEED, speedDownload, speedUpload)
            }
        }

    }

    private fun startTimer() {
        timer = Timer()

        timer?.schedule(object : TimerTask() {
            var count = 0

            override fun run() {
                count++
                val timeStr = String.format(
                    Locale.getDefault(), "%02d:%02d:%02d",
                    TimeUnit.SECONDS.toHours(count.toLong()),
                    TimeUnit.SECONDS.toMinutes(count.toLong()) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.SECONDS.toSeconds(count.toLong()) % TimeUnit.MINUTES.toSeconds(1))
                BroadcastUtil.sendTime(this@StatusService, AppKey.STATUS_CONNECT_TIME, timeStr)
                //sendTime(timeStr)
            }
        }, 0, 1500)
    }

    fun formatBytes(bytes: Long): String {
        val kb = 1024.0
        val mb = kb * 1024
        val gb = mb * 1024
        return when {
            bytes < kb -> "$bytes B/s"
            bytes < mb -> String.format("%.2f KB/s", bytes / kb)
            bytes < gb -> String.format("%.2f MB/s", bytes / mb)
            else -> String.format("%.2f GB/s", bytes / gb)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        job?.cancel()
    }
}
