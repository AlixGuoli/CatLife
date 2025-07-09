package com.runrick.vplifecat.ui

import com.runrick.vplifecat.base.BaseActivity
import com.runrick.vplifecat.base.BaseViewModel
import com.runrick.vplifecat.databinding.ActivityCountryBinding

class CountryActivity : BaseActivity<ActivityCountryBinding, BaseViewModel>() {
    override fun getViewBinding(): ActivityCountryBinding {
        return ActivityCountryBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<BaseViewModel> {
        return BaseViewModel::class.java
    }

    override fun initUI() {

    }

    override fun onClick() {
    }

}