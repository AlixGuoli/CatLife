package com.runrick.vplifecat.ui

import android.os.CountDownTimer
import androidx.lifecycle.lifecycleScope
import com.runrick.vplifecat.ads.AdsManager
import com.runrick.vplifecat.base.BaseActivity
import com.runrick.vplifecat.base.BaseViewModel
import com.runrick.vplifecat.databinding.ActivityBackBinding
import kotlinx.coroutines.launch

class BackActivity : BaseActivity<ActivityBackBinding, BaseViewModel>() {

    var time = 2 * 1000L
    private var timer: CountDownTimer? = null

    override fun getViewBinding(): ActivityBackBinding {
        return ActivityBackBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<BaseViewModel> {
        return BaseViewModel::class.java
    }

    override fun initUI() {


        timer = object : CountDownTimer(time, 10) {
            override fun onTick(millisUntilFinished: Long) {
                binding.progress.progress = (time/10 /2 - (millisUntilFinished / 10L)).toInt()
            }

            override fun onFinish() {
                AdsManager.showIntAd(this@BackActivity)
                finish()
            }
        }

        lifecycleScope.launch {
            timer?.start()
        }
    }

    override fun onClick() {

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