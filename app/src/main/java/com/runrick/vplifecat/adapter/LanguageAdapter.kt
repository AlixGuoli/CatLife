package com.runrick.vplifecat.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.runrick.vplifecat.R
import com.runrick.vplifecat.bean.CountryBean
import com.runrick.vplifecat.utils.BasicUtil

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/16
 */
class LanguageAdapter: BaseQuickAdapter<CountryBean, BaseViewHolder>(
    R.layout.item_country
) {
    override fun convert(holder: BaseViewHolder, item: CountryBean) {
        val itemLay = holder.getView<LinearLayout>(R.id.itemLay)
        val logo = holder.getView<ImageView>(R.id.logo)
        val name = holder.getView<TextView>(R.id.name)

        name.text = item.name


        val bitmap = BasicUtil.getCountryAssets(item.country)
        if (bitmap != null) {
            logo.setImageBitmap(bitmap)
        }

        if (item.isSelect) {
            itemLay.isSelected = true
        }else {
            itemLay.isSelected = false
        }
    }

    fun setAll(isSelect: Boolean) {
        data.forEach {
            it.isSelect = isSelect
        }
    }
}