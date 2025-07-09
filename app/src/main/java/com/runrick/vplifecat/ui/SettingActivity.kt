package com.runrick.vplifecat.ui

import android.content.Context
import android.content.Intent

import com.elvishew.xlog.XLog
import com.hjq.language.MultiLanguages
import com.hjq.language.OnLanguageListener
import com.runrick.vplifecat.base.BaseActivity
import com.runrick.vplifecat.base.BaseViewModel
import com.runrick.vplifecat.databinding.ActivitySettingBinding
import java.util.Locale

class SettingActivity : BaseActivity<ActivitySettingBinding, BaseViewModel>() {

    companion object {
        fun onNewIntent(context: Context, language: Boolean = false): Intent {
            val intent = Intent(context, SettingActivity::class.java)
            intent.putExtra("language", language)
            return intent
        }
    }

    override fun getViewBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<BaseViewModel> {
        return BaseViewModel::class.java
    }

    override fun initUI() {

    }

    override fun onClick() {
        MultiLanguages.setOnLanguageListener(object : OnLanguageListener {
            override fun onAppLocaleChange(oldLocale: Locale?, newLocale: Locale?) {
                XLog.e("onAppLocaleChange oldLocale: $oldLocale newLocale: $newLocale")
                finish()
                startActivity(onNewIntent(this@SettingActivity, true))
            }

            override fun onSystemLocaleChange(oldLocale: Locale?, newLocale: Locale?) {}
        })

        binding.back.setOnClickListener {
            finish()
        }

        binding.language.setOnClickListener {
            startActivity(Intent(this, LanguageActivity::class.java))
        }

       binding.rateUs.setOnClickListener {
           startActivity(Intent(this, RateActivity::class.java))
       }
    }
}