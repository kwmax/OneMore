package com.kwmax.onemore.http

import okhttp3.Interceptor

/**
 * created by kwmax on 2020/1/9
 * desc:
 */
interface IInterceptorCallback {
    fun getInterceptorList():List<Interceptor>
}