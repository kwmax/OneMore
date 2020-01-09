package com.kwmax.onemore.http

import java.lang.RuntimeException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * created by kwmax on 2020/1/9
 * desc:
 */
object SSLSocketClient {
    fun getSSLSocketFactory():SSLSocketFactory{
        try {
            var sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, getTrustManager(), SecureRandom())
            return sslContext.socketFactory
        } catch (e:Exception){
            throw RuntimeException(e)
        }
    }

    /**
     * 获取TrustManager
     */
    private fun getTrustManager():Array<TrustManager>{
        return arrayOf(object : X509TrustManager{
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
    }


    /**
     * 获取HostnameVerifier
     */
    fun getHostnameVerifier():HostnameVerifier{
        return HostnameVerifier { hostname, session -> true }
    }
}