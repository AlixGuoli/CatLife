package com.runrick.vplifecat.ui

import androidx.lifecycle.lifecycleScope
import com.gyf.immersionbar.ImmersionBar
import com.runrick.vplifecat.R
import com.runrick.vplifecat.base.BaseActivity
import com.runrick.vplifecat.base.BaseViewModel
import com.runrick.vplifecat.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>() {

    private var isConnecting = false
    private var isConneced = false

    override fun getViewBinding(): ActivityMainBinding {
        return  ActivityMainBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<BaseViewModel> {
        return BaseViewModel::class.java
    }

    override fun initUI() {
        ImmersionBar.with(this).transparentStatusBar().init()
    }

    override fun onClick() {
//        binding.btn.setOnClickListener {
//            startConnect()
//        }
//
//        binding.btn2.setOnClickListener {
//            stopConnect()
//        }
        binding.touch.setOnClickListener {

        }
    }

    private fun startConnect() {
        lifecycleScope.launch {
            isConnecting = true
            binding.lottie.setAnimation(R.raw.connecting)
            binding.lottie.playAnimation()
            delay(5000)
            binding.lottie.setAnimation(R.raw.connected)
            binding.lottie.playAnimation()
            isConnecting = false
            isConneced = true
        }
    }

    private fun stopConnect() {
        lifecycleScope.launch {
            isConnecting = true
            binding.lottie.setAnimation(R.raw.connecting)
            binding.lottie.playAnimation()
            delay(3000)
            binding.lottie.cancelAnimation()
            binding.lottie.setAnimation(R.raw.connectno)
            binding.lottie.playAnimation()
            isConnecting = false
            isConneced = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}