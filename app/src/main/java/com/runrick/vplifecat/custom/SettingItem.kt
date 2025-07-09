package com.runrick.vplifecat.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.runrick.vplifecat.R
import com.runrick.vplifecat.databinding.ItemSettingBinding
import androidx.core.content.withStyledAttributes

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/13
 */
class SettingItem(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    var title = ""
    var icon = 0

    var binding: ItemSettingBinding

    init {
        binding = ItemSettingBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.item_setting,this, true)
        )
        context.withStyledAttributes(attrs, R.styleable.SettingItem) {
            icon = getResourceId(R.styleable.SettingItem_icon, 0)
            title = getString(R.styleable.SettingItem_title) ?: ""

            if (icon != 0) {
                binding.icon.setImageResource(icon)
            }
            binding.title.text = title
        }
    }
}