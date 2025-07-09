package com.runrick.vplifecat.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.runrick.vplifecat.R

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/16
 */
class MediumText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyle: Int = 0
) : AppCompatTextView(context, attrs, defaultStyle) {

    init {
        // 使用真正的 Medium 字体
        typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
    }
}
