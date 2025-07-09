package com.runrick.vplifecat.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import me.drakeet.support.toast.ToastCompat

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/13
 */

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun Context.toast(message: Int): Toast = ToastCompat
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }

const val threshold = 1000
const val divisor = 1024F

@SuppressLint("DefaultLocale")
fun Long.formatSpeed(): String {
    val kb = 1024.0
    val mb = kb * 1024
    val gb = mb * 1024
    return when {
        this < kb -> "$this B/s"
        this < mb -> String.format("%.2f KB/s", this / kb)
        this < gb -> String.format("%.2f MB/s", this / mb)
        else -> String.format("%.2f GB/s", this / gb)
    }
}

@SuppressLint("DefaultLocale")
fun Long.formatBytes(): String {
    val kb = 1024.0
    val mb = kb * 1024
    val gb = mb * 1024
    return when {
        this < kb -> "$this"
        this < mb -> String.format("%.2f", this / kb)
        this < gb -> String.format("%.2f", this / mb)
        else -> String.format("%.2f", this / gb)
    }
}

fun Long.formatBytesUnit(): String {
    val kb = 1024.0
    val mb = kb * 1024
    val gb = mb * 1024
    return when {
        this < kb -> "B/s"
        this < mb -> "KB/s"
        this < gb -> "MB/s"
        else -> "GB/s"
    }
}

fun Long.toSpeedString() = toTrafficString() + "/s"

fun Long.toTrafficString(): String {
    if (this == 0L)
        return "\t\t\t0\t  B"

    if (this < threshold)
        return "${this.toFloat().toShortString()}\t  B"

    val kib = this / divisor
    if (kib < threshold)
        return "${kib.toShortString()}\t KB"

    val mib = kib / divisor
    if (mib < threshold)
        return "${mib.toShortString()}\t MB"

    val gib = mib / divisor
    if (gib < threshold)
        return "${gib.toShortString()}\t GB"

    val tib = gib / divisor
    if (tib < threshold)
        return "${tib.toShortString()}\t TB"

    val pib = tib / divisor
    if (pib < threshold)
        return "${pib.toShortString()}\t PB"

    return "∞"
}

private fun Float.toShortString(): String {
    val s = "%.2f".format(this)
    if (s.length <= 4)
        return s
    return s.substring(0, 4).removeSuffix(".")
}