package com.runrick.vplifecat.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/12
 */
@SuppressLint("StaticFieldLeak")
object AppHelper {

    lateinit var instance: Context

    fun init(app: Application) {
        instance = app.applicationContext
    }

    fun AppHelper.string(resId:Int):String{
        return this.instance.resources.getString(resId)
    }

    fun AppHelper.color(resId:Int): Int{
        return this.instance.getColor(resId)
    }
}