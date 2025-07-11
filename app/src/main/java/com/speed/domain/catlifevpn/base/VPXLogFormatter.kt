package com.speed.domain.catlifevpn.base

import com.elvishew.xlog.formatter.border.DefaultBorderFormatter
import com.elvishew.xlog.internal.SystemCompat

/**
 * @Description:
 * @Author: Alix
 * @Date: 2024/3/11
 */
class VPXLogFormatter: DefaultBorderFormatter() {
    override fun format(segments: Array<String?>?): String? {
        var formatted = super.format(segments)
        if (formatted.isNotEmpty()) {
            formatted = " " + SystemCompat.lineSeparator + formatted
        }
        return formatted
    }
}