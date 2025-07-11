package com.speed.domain.catlifevpn.ui

import com.speed.domain.catlifevpn.base.BaseActivity
import com.speed.domain.catlifevpn.databinding.ActivityConnectResultBinding
import com.speed.domain.catlifevpn.viewmodel.MainViewModel

class ConnectResultActivity : BaseActivity<ActivityConnectResultBinding, MainViewModel>() {
    override fun getViewBinding(): ActivityConnectResultBinding {
        return ActivityConnectResultBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun initUI() {

    }

    override fun onClick() {
        binding.close.setOnClickListener {
            finish()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }
    }
}