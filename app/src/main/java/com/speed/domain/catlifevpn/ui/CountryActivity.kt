package com.speed.domain.catlifevpn.ui

import com.speed.domain.catlifevpn.base.BaseActivity
import com.speed.domain.catlifevpn.base.BaseViewModel
import com.speed.domain.catlifevpn.databinding.ActivityCountryBinding

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