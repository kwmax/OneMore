package com.kwmax.onemore.http

import android.util.Log
import com.kwmax.onemore.BASE_URL
import com.kwmax.onemore.BuildConfig
import com.kwmax.onemore.HTTP_TIME_OUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * created by kwmax on 2020/1/9
 * desc:
 */
class RetrofitClient private constructor() {

    companion object {

        private val instance by lazy { RetrofitClient() }

        val api: ApiService by lazy { RetrofitClient.instance.buildApi() }
    }

    private fun buildApi() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient())
        .build()
        .create(ApiService::class.java)


    private fun okHttpClient() = OkHttpClient.Builder()
        .connectTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor())
        .build()

}