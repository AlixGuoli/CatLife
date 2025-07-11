package com.speed.domain.catlifevpn.utils

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.elvishew.xlog.XLog
import com.speed.domain.catlifevpn.MyApp
import java.io.IOException

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/16
 */
object BasicUtil {
    fun getCountryAssets(country: String): Bitmap? {
        var realCountry = country
        if (country.equals("UK", false)) {
            realCountry = "GB"
        }
        val assetManager: AssetManager = MyApp.getApp().applicationContext.assets
        try {
            val stream = assetManager.open("country/$realCountry.png")
            return BitmapFactory.decodeStream(stream)
        }catch (e: IOException) {
            e.printStackTrace()
            XLog.e(e)
        }
        return null
    }
}