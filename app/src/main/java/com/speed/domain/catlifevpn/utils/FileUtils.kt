package com.speed.domain.catlifevpn.utils

import com.speed.domain.catlifevpn.MyApp
import com.speed.domain.catlifevpn.R
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader

/**
    * @Description: 
    * @Author: Alix
    * @Date: 2025/6/23
 */
object FileUtils {
    fun readLocalAdFile(): String {
        val sb = StringBuilder()
        val openRawResource = MyApp.getApp().resources.openRawResource(R.raw.config)
        try {
            val br = BufferedReader(InputStreamReader(openRawResource, "UTF-8"))
            var line: String? = null
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            br.close()
            openRawResource.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return sb.toString()
    }
}