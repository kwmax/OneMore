package com.kwmax.onemore.http

import android.content.Context
import android.util.LruCache
import com.kwmax.onemore.BASE_URL
import com.kwmax.onemore.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * created by kwmax on 2020/1/9
 * desc:
 */
class RetrofitClient {

    var retrofit: Retrofit? = null
    var serviceCache: LruCache<String, Any> = LruCache(20)

    companion object {
        const val READ_TIME_OUT: Long = 10L
        const val WRITE_TIME_OUT: Long = 10L
        const val CONNECT_TIME_OUT: Long = 10L
        fun get(): RetrofitClient {
            return RequestManagerHolder.INSTANCE
        }
    }

    constructor()

    class RequestManagerHolder {
        companion object {
            internal var INSTANCE = RetrofitClient()
        }
    }

    fun init(context: Context) {
        init(context, GsonConverterFactory.create(), null)
    }

    fun init(context: Context, factory: Converter.Factory) {
        init(context, factory, null)
    }

    fun init(context: Context, interceptorCallback: IInterceptorCallback) {
        init(context, GsonConverterFactory.create(), interceptorCallback)
    }

    fun init(
        context: Context,
        factory: Converter.Factory?,
        interceptorCallback: IInterceptorCallback?
    ) {
        synchronized(this@RetrofitClient) {
            val builder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient(context, interceptorCallback))
            if (factory == null) {
                builder.addConverterFactory(GsonConverterFactory.create())
            } else {
                builder.addConverterFactory(factory)
            }
            retrofit = builder.build()
        }
    }

    private fun getOkHttpClient(
        context: Context,
        iInterceptorCallback: IInterceptorCallback?
    ): OkHttpClient {
        var loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        var builder = OkHttpClient.Builder()
            .cache(Cache(context.getDir("http_cache", Context.MODE_PRIVATE), 1024 * 1024 * 100))
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(BaseUrlInterceptorKt())
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .retryOnConnectionFailure(false)
        addInterceptor(builder, iInterceptorCallback)
        return builder.build()
    }

    private fun addInterceptor(
        builder: OkHttpClient.Builder,
        interceptorCallback: IInterceptorCallback?
    ) {
        if (null != interceptorCallback) {
            var interceptorList = interceptorCallback.getInterceptorList()
            if (null != interceptorList && interceptorList.isNotEmpty()) {
                for (interceptor in interceptorList) {
                    if (null != null) {
                        builder.addInterceptor(interceptor)
                    }
                }
            }
        }
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(LogInterceptor())
        }
    }

    fun <T> create(cls: Class<T>): T? {
        var any = serviceCache.get(cls.simpleName)
        var t: T?
        if (null != any) {
            try {
                t = serviceCache.get(cls.simpleName) as T
            } catch (e: Throwable) {
                t = retrofit?.create(cls)
                serviceCache.put(cls.simpleName, t)
            }
        } else {
            t = retrofit?.create(cls)
            serviceCache.put(cls.simpleName, t)
        }
        return t
    }

}