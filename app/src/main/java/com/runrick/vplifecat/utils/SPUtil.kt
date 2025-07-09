package com.runrick.vplifecat.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/13
 */
class SPUtil<T>(private val key: String, private val default: T) : ReadWriteProperty<Any?, T> {

    companion object {
        val sp: SharedPreferences by lazy {
            Utils.getApp().getSharedPreferences(
                AppKey.TAG_SP,
                Context.MODE_PRIVATE
            )
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSaveValues(key, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        return saveValues(key, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun <T> saveValues(key: String, default: T) = with(sp.edit()) {
        when (default) {
            is String -> putString(key, default)
            is Int -> putInt(key, default)
            is Boolean -> putBoolean(key, default)
            is Long -> putLong(key, default)
            is Float -> putFloat(key, default)
            else -> {
                val jsonSrc = Gson().toJson(default)
                putString(key, jsonSrc)
            }
        }.apply()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getSaveValues(key: String, default: T) = with(sp) {
        val value: Any? = when (default) {
            is String -> getString(key, default)
            is Int -> getInt(key, default)
            is Boolean -> getBoolean(key, default)
            is Long -> getLong(key, default)
            is Float -> getFloat(key, default)
            else -> {
                val jsonSrc = getString(key, "")
                Gson().fromJson<T>(jsonSrc, object : TypeToken<T>() {}.type)
            }
        }
        value as T
    }
}