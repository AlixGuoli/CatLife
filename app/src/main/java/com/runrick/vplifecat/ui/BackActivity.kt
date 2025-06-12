package com.runrick.vplifecat.ui

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
        ImmersionBar.with(this).transparentStatusBar().init()

        timer = object : CountDownTimer(time, 200) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
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