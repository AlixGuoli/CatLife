package com.speed.domain.catlifevpn.ui

import android.content.Intent
import android.os.CountDownTimer
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.SPUtils
import com.elvishew.xlog.XLog
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.speed.domain.catlifevpn.ads.AdsManager
import com.speed.domain.catlifevpn.base.BaseActivity
import com.speed.domain.catlifevpn.base.BaseViewModel
import com.speed.domain.catlifevpn.databinding.ActivitySplashBinding
import com.speed.domain.catlifevpn.utils.AppKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class SplashActivity : BaseActivity<ActivitySplashBinding, BaseViewModel>() {

    var time = 2 * 1000L
    private var timer: CountDownTimer? = null

    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<BaseViewModel> {
        return BaseViewModel::class.java
    }

    override fun initUI() {
        loadData()

        timer = object : CountDownTimer(time, 200) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                next()
                if (AdsManager.mInterstitialAd != null) {
                    AdsManager.showIntAd(this@SplashActivity)
                }
            }
        }

        lifecycleScope.launch {
            timer?.start()
        }
    }

    override fun onClick() {

    }

    private fun loadData() {
        lifecycleScope.launch {
            AdsManager.loadIntAd(this@SplashActivity)
            withContext(Dispatchers.IO) {
                newUser()
            }
            timer?.start()
        }
    }

    private fun newUser() {
        try {
            var uuid = SPUtils.getInstance().getString(AppKey.KEY_USER_ID)
            //XLog.e("user_id ${uuid}")
            if (uuid.isNullOrEmpty()) {
                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this)
                uuid = if (!adInfo.id.isNullOrEmpty()) {
                    XLog.e("user_id ${adInfo.id}")
                    adInfo.id
                } else {
                    UUID.randomUUID().toString()
                }
                SPUtils.getInstance().put(AppKey.KEY_USER_ID, uuid)
            }
        } catch (e: Exception) {
            var uuid = UUID.randomUUID().toString()
            SPUtils.getInstance().put(AppKey.KEY_USER_ID, uuid)
        }
    }

    private fun next() {
        timer?.cancel()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        try {
            timer?.cancel()
            timer = null
        } catch (e: Exception) {

        }
    }
}