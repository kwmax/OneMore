package com.kwmax.onemore.http

import com.kwmax.onemore.BASE_URL
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * created by kwmax on 2020/1/9
 * desc:
 */
class BaseUrlInterceptorKt :Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()
        var oldUrl = originalRequest.url()
        var builder = originalRequest.newBuilder()
        var urlNameList = originalRequest.headers("url")
        if (null != urlNameList && urlNameList.isNotEmpty()){
            builder.removeHeader("url")
            var urlName = urlNameList[0]
            var baseUrl = HttpUrl.parse(BASE_URL)
            var newHttpUrl = oldUrl.newBuilder()
                    .scheme(baseUrl!!.scheme())
                    .host(baseUrl!!.host())
                    .port(baseUrl!!.port())
                    .setPathSegment(0,baseUrl.pathSegments()[0])
                    .build()
            var newRequest = builder.url(newHttpUrl).build()
            return chain.proceed(newRequest)
        } else{
            return chain.proceed(originalRequest)
        }
    }
}