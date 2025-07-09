package com.runrick.vplifecat.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.runrick.vplifecat.MyApp
import com.runrick.vplifecat.R
import com.runrick.vplifecat.bean.ServerConfig
import com.runrick.vplifecat.bean.V2rayConfig
import com.runrick.vplifecat.ui.MainActivity
import com.runrick.vplifecat.utils.AppKey
import com.runrick.vplifecat.utils.FileUtils
import com.runrick.vplifecat.utils.MessageUtil
import com.runrick.vplifecat.utils.MmkvManager
import com.runrick.vplifecat.utils.Utils
import com.runrick.vplifecat.utils.formatSpeed
import com.runrick.vplifecat.utils.toast
import com.tencent.mmkv.MMKV
import go.Seq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import libv2ray.Libv2ray
import libv2ray.V2RayPoint
import libv2ray.V2RayVPNServiceSupportsSet
import java.lang.ref.SoftReference
import java.util.concurrent.Flow

/**
    * @Description: 
    * @Author: Alix
    * @Date: 2025/6/20
 */
object V2RayServiceManager {

    private const val NOTIFICATION_ID = 1
    private const val NOTIFICATION_PENDING_INTENT_CONTENT = 0
    private const val NOTIFICATION_PENDING_INTENT_STOP_V2RAY = 1
    private const val NOTIFICATION_ICON_THRESHOLD = 3000

    val v2rayPoint: V2RayPoint = Libv2ray.newV2RayPoint(V2RayCallback(), Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
    private val mainStorage by lazy { MMKV.mmkvWithID(MmkvManager.ID_MAIN, MMKV.MULTI_PROCESS_MODE) }
    private val settingsStorage by lazy { MMKV.mmkvWithID(MmkvManager.ID_SETTING, MMKV.MULTI_PROCESS_MODE) }

    var serviceControl: SoftReference<ServiceControl>? = null
        set(value) {
            field = value
            Seq.setContext(value?.get()?.getService()?.applicationContext)
            Libv2ray.initV2Env(Utils.userAssetPath(value?.get()?.getService()), "")
        }
    var currentConfig: ServerConfig? = null
    var v2rayConfig: V2rayConfig? = null

    private var lastQueryTime = 0L
    private var mBuilder: NotificationCompat.Builder? = null
    private var mSubscription: Flow.Subscription? = null
    private var mNotificationManager: NotificationManager? = null

    fun startV2Ray(context: Context) {
        if (settingsStorage?.decodeBool(AppKey.PREF_PROXY_SHARING) == true) {
            context.toast(R.string.toast_warning_pref_proxysharing_short)
        } else {
            context.toast(R.string.toast_services_start)
        }
        val intent = if (settingsStorage?.decodeString(AppKey.PREF_MODE) ?: "VPN" == "VPN") {
            Intent(context.applicationContext, V2RayVpnService::class.java)
        } else {
            Intent(context.applicationContext, V2RayProxyOnlyService::class.java)
        }
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
//            context.startForegroundService(intent)
//        } else {
//            context.startService(intent)
//        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            }else{
                context.startService(intent)
            }
        }catch (e : java.lang.Exception){
            XLog.e("Exception when starting service: ${e.message}")
        }
    }

    fun startV2rayPoint() {
        XLog.e("8888 startV2rayPoint --- v2rayPoint.isRunning: ${v2rayPoint.isRunning}")
        val service = serviceControl?.get()?.getService() ?: return
        //val guid = mainStorage?.decodeString(MmkvManager.KEY_SELECTED_SERVER) ?: return
        //val config = MmkvManager.decodeServerConfig(guid) ?: return
        if (!v2rayPoint.isRunning) {
            val configJson = FileUtils.readLocalAdFile()
            val config = Gson().fromJson(configJson, V2rayConfig::class.java)
            v2rayConfig = config

            registerMyReceiver()
            XLog.e("configJson: $configJson")
            v2rayPoint.configureFileContent = configJson
            XLog.e("domainName:" + getV2rayPointDomainAndPort(config))
            v2rayPoint.domainName = getV2rayPointDomainAndPort(config)

            try {
                v2rayPoint.runLoop(settingsStorage?.decodeBool(AppKey.PREF_PREFER_IPV6) ?: false)
            } catch (e: Exception) {
                XLog.e("v2ray run error: $e")
                XLog.d(e.toString())
            }

            if (v2rayPoint.isRunning) {
                MessageUtil.sendMsg2UI(service, AppKey.MSG_STATE_START_SUCCESS, "")
                //showNotification()
            } else {
                MessageUtil.sendMsg2UI(service, AppKey.MSG_STATE_START_FAILURE, "")
                cancelNotification()
            }
        }
    }

    fun getV2rayPointDomainAndPort(config: V2rayConfig): String {
        val address = config.getProxyOutbound()?.getServerAddress().orEmpty()
        val port = config.getProxyOutbound()?.getServerPort()

        return if (Utils.isIpv6Address(address)) {
            String.format("[%s]:%s", address, port)
        } else {
            String.format("%s:%s", address, port)
        }
    }

    fun stopV2rayPoint() {
        val service = serviceControl?.get()?.getService() ?: return

        if (v2rayPoint.isRunning) {
            GlobalScope.launch(Dispatchers.Default) {
                try {
                    v2rayPoint.stopLoop()
                } catch (e: Exception) {
                    XLog.d(e.toString())
                }
            }
        }

        MessageUtil.sendMsg2UI(service, AppKey.MSG_STATE_STOP_SUCCESS, "")
        cancelNotification()

        try {
            service.unregisterReceiver(mMsgReceive)
        } catch (e: Exception) {
            XLog.d(e.toString())
        }
    }

    fun showNotification() {
        val service = serviceControl?.get()?.getService() ?: return
        val mainIntent = Intent(service, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            service,
            NOTIFICATION_PENDING_INTENT_CONTENT,
            mainIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        val channelId = MyApp.getApp().packageName
        val channelName = "VPN Service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.DKGRAY
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            }
            getNotificationManager()?.createNotificationChannel(channel)
        }

        mBuilder = NotificationCompat.Builder(service, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(service.getString(R.string.app_name))
            .setContentText("")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setOngoing(true)
            .setShowWhen(false)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        service.startForeground(NOTIFICATION_ID, mBuilder?.build())
    }

    private fun updateNotification(byteDownload: Long, byteUpload: Long) {
        if (mBuilder != null) {
            val contentText =
                "Download: ${byteDownload.formatSpeed()} ↓    Upload: ${byteUpload.formatSpeed()} ↑"
            // Emui4.1 need content text even if style is set as BigTextStyle
            mBuilder?.setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            mBuilder?.setContentText(contentText)
            getNotificationManager()?.notify(NOTIFICATION_ID, mBuilder?.build())
        }
    }

    fun cancelNotification() {
        val service = serviceControl?.get()?.getService() ?: return
        service.stopForeground(true)
        mBuilder = null
    }

    private fun getNotificationManager(): NotificationManager? {
        if (mNotificationManager == null) {
            val service = serviceControl?.get()?.getService() ?: return null
            mNotificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return mNotificationManager
    }

    private fun registerMyReceiver() {
        val service = serviceControl?.get()?.getService() ?: return
        try {
            val mFilter = IntentFilter(AppKey.BROADCAST_ACTION_SERVICE)
            mFilter.addAction(Intent.ACTION_SCREEN_ON)
            mFilter.addAction(Intent.ACTION_SCREEN_OFF)
            mFilter.addAction(Intent.ACTION_USER_PRESENT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                service.registerReceiver(mMsgReceive, mFilter, Context.RECEIVER_EXPORTED)
            } else {
                ContextCompat.registerReceiver(
                    service,
                    mMsgReceive,
                    mFilter,
                    ContextCompat.RECEIVER_NOT_EXPORTED
                )
            }

            val statusFilter = IntentFilter(AppKey.BROADCAST_ACTION_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                service.registerReceiver(statusReceiver, statusFilter, Context.RECEIVER_EXPORTED)
            } else {
                ContextCompat.registerReceiver(
                    service,
                    statusReceiver,
                    statusFilter,
                    ContextCompat.RECEIVER_NOT_EXPORTED
                )
            }
        } catch (e: Exception) {
            XLog.e(e.toString())
        }
    }

    private class V2RayCallback : V2RayVPNServiceSupportsSet {
        override fun shutdown(): Long {
            val serviceControl = serviceControl?.get() ?: return -1
            // called by go
            return try {
                serviceControl.stopService()
                0
            } catch (e: Exception) {
                XLog.d(e.toString())
                -1
            }
        }

        override fun prepare(): Long {
            return 0
        }

        override fun protect(l: Long): Boolean {
            val serviceControl = serviceControl?.get() ?: return true
            return serviceControl.vpnProtect(l.toInt())
        }

        override fun onEmitStatus(l: Long, s: String?): Long {
            //Logger.d(s)

            return 0
        }

        override fun setup(s: String): Long {
            val serviceControl = serviceControl?.get() ?: return -1
            //Logger.d(s)
            return try {
                serviceControl.startService()
                lastQueryTime = System.currentTimeMillis()
                //startSpeedNotification()
                0
            } catch (e: Exception) {
                XLog.d(e.toString())
                -1
            }
        }
    }

    private val statusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.getIntExtra("key", 0)) {
                AppKey.STATUS_CONNECT_TIME -> {

                }

                AppKey.STATUS_SPEED -> {
                    val byteDownload = intent.getLongExtra("download", 0L)
                    val byteUpload = intent.getLongExtra("upload", 0L)
                    updateNotification(byteDownload, byteUpload)
                }
            }
        }
    }

    private val mMsgReceive = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            val serviceControl = serviceControl?.get() ?: return
            when (intent?.getIntExtra("key", 0)) {
                AppKey.MSG_REGISTER_CLIENT -> {
                    //Logger.e("ReceiveMessageHandler", intent?.getIntExtra("key", 0).toString())
                    if (v2rayPoint.isRunning) {
                        MessageUtil.sendMsg2UI(serviceControl.getService(), AppKey.MSG_STATE_RUNNING, "")
                    } else {
                        MessageUtil.sendMsg2UI(serviceControl.getService(), AppKey.MSG_STATE_NOT_RUNNING, "")
                    }
                }
                AppKey.MSG_UNREGISTER_CLIENT -> {
                    // nothing to do
                }
                AppKey.MSG_STATE_START -> {
                    // nothing to do
                }
                AppKey.MSG_STATE_STOP -> {
                    serviceControl.stopService()
                }
                AppKey.MSG_STATE_RESTART -> {
                    startV2rayPoint()
                }
                AppKey.MSG_MEASURE_DELAY -> {
                    //measureV2rayDelay()
                }
            }

            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    XLog.d("SCREEN_OFF, stop querying stats")
                    //stopSpeedNotification()
                }
                Intent.ACTION_SCREEN_ON -> {
                    XLog.d( "SCREEN_ON, start querying stats")
                    //startSpeedNotification()
                }
            }
        }
    }
}