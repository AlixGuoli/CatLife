package com.speed.domain.catlifevpn.ui

import android.widget.Toast
import com.elvishew.xlog.XLog
import com.speed.domain.catlifevpn.MyApp
import com.speed.domain.catlifevpn.R
import com.speed.domain.catlifevpn.base.BaseActivity
import com.speed.domain.catlifevpn.base.BaseViewModel
import com.speed.domain.catlifevpn.databinding.ActivityRateBinding

class RateActivity : BaseActivity<ActivityRateBinding, BaseViewModel>() {

    private var currentStars = 0

    override fun getViewBinding(): ActivityRateBinding {
        return ActivityRateBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<BaseViewModel> {
        return BaseViewModel::class.java
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

        binding.btnFeedback.setOnClickListener {
            if (currentStars != 0) {
                Toast.makeText(this, "Rate：$currentStars Stars", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, MyApp.getApp().getString(R.string.toast_not_rate), Toast.LENGTH_SHORT).show()
            }
        }

        binding.ratingView.onRatingChanged = { stars ->
            XLog.e("评分：$stars 星")
            currentStars = stars
            //Toast.makeText(this, "评分：$stars 星", Toast.LENGTH_SHORT).show()
        }
    }

}