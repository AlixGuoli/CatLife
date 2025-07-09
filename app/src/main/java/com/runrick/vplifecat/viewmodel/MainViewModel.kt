package com.runrick.vplifecat.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.elvishew.xlog.XLog
import com.runrick.vplifecat.MyApp
import com.runrick.vplifecat.base.BaseViewModel
import com.runrick.vplifecat.utils.AppKey
import com.runrick.vplifecat.utils.CacheValue

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/18
 */
class MainViewModel: BaseViewModel() {

    var connectTime = MutableLiveData<String>()


    fun startMsgBroadcast() {
        val intentFilter = IntentFilter().apply {
            addAction(AppKey.BROADCAST_ACTION_ACTIVITY)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            MyApp.getApp()
                .registerReceiver(mMsgReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            ContextCompat.registerReceiver(
                MyApp.getApp(),
                mMsgReceiver,
                intentFilter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    fun startStatusBroadcast() {
        val intentFilter = IntentFilter().apply {
            addAction(AppKey.BROADCAST_ACTION_STATUS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            MyApp.getApp()
                .registerReceiver(mStatusReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            ContextCompat.registerReceiver(
                MyApp.getApp(),
                mStatusReceiver,
                intentFilter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    override fun onCleared() {
        try {
            XLog.e("MainViewModel onCleared")
            MyApp.getApp().unregisterReceiver(mMsgReceiver)
            MyApp.getApp().unregisterReceiver(mStatusReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
            XLog.e(e.toString())
        }
        super.onCleared()
    }

    private val mMsgReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.getIntExtra("key", 0)) {
                AppKey.MSG_STATE_RUNNING -> {
                    XLog.e("MSG_STATE_RUNNING")
                    CacheValue.isRunning.value = true
                }

                AppKey.MSG_STATE_NOT_RUNNING -> {
                    XLog.e("MSG_STATE_NOT_RUNNING")
                    CacheValue.isRunning.value = false
                }

                AppKey.MSG_STATE_START_SUCCESS -> {
                    XLog.e("MSG_STATE_START_SUCCESS")
                    //ToastUtils.showShort(R.string.toast_services_success)
                    CacheValue.isRunning.value = true
                    //connectTime.value = System.currentTimeMillis()
                    //XLog.e("postValue ${connectTime.value}")
                }

                AppKey.MSG_STATE_START_FAILURE -> {
                    XLog.e("MSG_STATE_START_FAILURE")
                    //ToastUtils.showShort(R.string.toast_services_failure)
                    CacheValue.isRunning.value = false
                }

                AppKey.MSG_STATE_STOP_SUCCESS -> {
                    XLog.e("MSG_STATE_STOP_SUCCESS")
                    CacheValue.isRunning.value = false
                }

                AppKey.MSG_MEASURE_DELAY_SUCCESS -> {
//                    CacheValue.speedDelay.value = intent.getStringExtra("content") ?: "0 ms"
//                    XLog.e("MSG_MEASURE_DELAY_SUCCESS $speedText")
                }

            }
        }
    }

    private val mStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.getIntExtra("key", 0)) {
                AppKey.STATUS_CONNECT_TIME -> {
                    val timeStr = intent?.getStringExtra("time")
                    timeStr?.let {
                        connectTime.value = it
                        //XLog.e("postValue ${connectTime.value}")
                    }
                }

                AppKey.STATUS_SPEED -> {
//                    val byteDownload = intent.getLongExtra("download", 0L)
//                    val byteUpload = intent.getLongExtra("upload", 0L)
//                    textDownload = byteDownload.formatBytes()
//                    textUpload = byteUpload.formatBytes()
//                    unitDownloadText = byteDownload.formatBytesUnit()
//                    unitUploadText = byteUpload.formatBytesUnit()
//                    speedStatus.value = true
                }
            }
        }
    }
}