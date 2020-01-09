package com.kwmax.onemore.http

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * created by kwmax on 2020/1/9
 * desc:
 */
class LogInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var t1 = System.nanoTime()
        Log.d("LogInterceptor", String.format(
                "Sending request %s on %s%n%s",
                request.url(),
                chain.connection(),
                request.headers()))
        var response = chain.proceed(request)
        var t2 = System.nanoTime()
        Log.d("LogInterceptor", String.format(
                "Received response for %s in %.1fms%n%s",
                response.request().url(),
                (t2 - t1).toDouble() / 1000000.0,
                response.headers()))
        return response
    }
}