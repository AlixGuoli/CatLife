package com.runrick.vplifecat.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.gyf.immersionbar.ImmersionBar
import com.runrick.vplifecat.R
import com.runrick.vplifecat.base.BaseActivity
import com.runrick.vplifecat.base.BaseViewModel
import com.runrick.vplifecat.databinding.ActivitySplashBinding
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding, BaseViewModel>() {

    var time = 3 * 1000L
    private var timer: CountDownTimer? = null

    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<BaseViewModel> {
        return BaseViewModel::class.java
    }

    override fun initUI() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .init()

        timer = object : CountDownTimer(time, 200) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                next()
            }
        }

        lifecycleScope.launch {
            timer?.start()
        }
    }

    override fun onClick() {

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