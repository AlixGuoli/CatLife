package com.speed.domain.catlifevpn.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.language.MultiLanguages
import com.speed.domain.catlifevpn.adapter.LanguageAdapter
import com.speed.domain.catlifevpn.base.BaseActivity
import com.speed.domain.catlifevpn.base.BaseViewModel
import com.speed.domain.catlifevpn.bean.CountryBean
import com.speed.domain.catlifevpn.databinding.ActivityLanguageBinding
import java.util.Locale

class LanguageActivity : BaseActivity<ActivityLanguageBinding, BaseViewModel>() {

    private val adapter by lazy {
        LanguageAdapter()
    }

    override fun getViewBinding(): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<BaseViewModel> {
        return BaseViewModel::class.java
    }

    override fun initUI() {

        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter

        val currentLanguage = MultiLanguages.getAppLanguage()
        val list = mutableListOf<CountryBean>()

        val de = CountryBean("DE", "de", "Deutsch")
        val en = CountryBean("GB", "en", "English")
        val es = CountryBean("ES", "es", "Español")
        val fr = CountryBean("FR", "fr", "Français")
        val in_ = CountryBean("ID", "in", "indonesia")
        val ja = CountryBean("JP", "ja", "日本語")
        val ko = CountryBean("KR", "ko", "한국어")
        val pt = CountryBean("PT", "pt", "Português")
        val ru = CountryBean("RU", "ru", "Русский")
        val zh = CountryBean("CN", "zh", "简体中文")

        list.add(de)
        list.add(en)
        list.add(es)
        list.add(fr)
        list.add(in_)
        list.add(ja)
        list.add(ko)
        list.add(pt)
        list.add(ru)
        list.add(zh)

        list.find {
            it.language == currentLanguage.language
        }?.isSelect = true

        adapter.data = list
        adapter.notifyDataSetChanged()
    }

    override fun onClick() {
        binding.back.setOnClickListener {
            finish()
        }

        adapter.setOnItemClickListener { _, _, position ->
            val item = adapter.getItem(position)
            adapter.setAll(false)
            item.isSelect = true
            adapter.notifyDataSetChanged()
            item.let {
                MultiLanguages.setAppLanguage(this, Locale(it.language))
                finish()
            }
        }
    }
}