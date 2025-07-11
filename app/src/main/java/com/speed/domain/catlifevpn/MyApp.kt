package com.speed.domain.catlifevpn

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.XLog
import com.elvishew.xlog.formatter.message.json.DefaultJsonFormatter
import com.elvishew.xlog.formatter.message.throwable.DefaultThrowableFormatter
import com.elvishew.xlog.formatter.message.xml.DefaultXmlFormatter
import com.elvishew.xlog.formatter.stacktrace.DefaultStackTraceFormatter
import com.elvishew.xlog.formatter.thread.DefaultThreadFormatter
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.Printer
import com.google.android.gms.ads.MobileAds
import com.hjq.language.MultiLanguages
import com.speed.domain.catlifevpn.ads.AdsManager
import com.speed.domain.catlifevpn.base.AppHelper
import com.speed.domain.catlifevpn.base.VPXLogFormatter
import com.speed.domain.catlifevpn.ui.BackActivity
import com.tencent.mmkv.MMKV

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/11
 */
class MyApp: Application(), Application.ActivityLifecycleCallbacks {

    private var count = 0

    companion object {
        var currentActivity: Activity? = null
        private lateinit var instance: MyApp
        var isBack = false

        fun getApp(): MyApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks(this)
        AppHelper.init(this)
        MultiLanguages.init(this)
        MMKV.initialize(this)
        MobileAds.initialize(this) {}
        initXLog()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
        if (isBack) {
            if (isCanShow()) {
                activity.startActivity(Intent(activity, BackActivity::class.java))
            }
        }
        count++
        isBack = false
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        count--
        if (count == 0) {
            isBack = true
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    fun isCanShow(): Boolean {
        return (AdsManager.checkCacheAd(AdsManager.AD_TYPE_INTER) && !AdsManager.isIntShowing)
    }

    fun initXLog() {
        val config = LogConfiguration.Builder()
            .tag("VP") // 指定 TAG，默认为 "X-LOG"
            .enableThreadInfo() // 允许打印线程信息，默认禁止
            .enableStackTrace(1) // Enable stack trace info with depth 2, disabled by default
            .enableBorder() // Enable border, disabled by default
            .borderFormatter(VPXLogFormatter())
            .jsonFormatter(DefaultJsonFormatter()) // Default: DefaultJsonFormatter
            .xmlFormatter(DefaultXmlFormatter()) // Default: DefaultXmlFormatter
            .throwableFormatter(DefaultThrowableFormatter()) // Default: DefaultThrowableFormatter
            .threadFormatter(DefaultThreadFormatter()) // Default: DefaultThreadFormatter
            .stackTraceFormatter(DefaultStackTraceFormatter()) // Default: DefaultStackTraceFormatter
            .build()
        val androidPrinter: Printer =
            AndroidPrinter() // Printer that print the log using android.util.Log
        XLog.init(config, androidPrinter)
    }
}