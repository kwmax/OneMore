package com.kwmax.onemore

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * created by kwmax on 2020/1/9
 * desc:
 */
class OneMoreApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: Context
    }

}