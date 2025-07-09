package com.runrick.vplifecat.ui

import com.runrick.vplifecat.base.BaseActivity
import com.runrick.vplifecat.databinding.ActivityConnectResultBinding
import com.runrick.vplifecat.viewmodel.MainViewModel

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