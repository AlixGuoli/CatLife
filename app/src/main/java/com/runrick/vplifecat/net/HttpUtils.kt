package com.runrick.vplifecat.net

import com.runrick.vplifecat.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/7/8
 */
object HttpUtils: CoroutineScope {

    private var client: OkHttpClient

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    init {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        client = OkHttpClient.Builder().apply {
            callTimeout(10, TimeUnit.SECONDS)
//            connectTimeout(30, TimeUnit.SECONDS)
//            readTimeout(30, TimeUnit.SECONDS)
//            writeTimeout(30, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
        }.build()

    }

}