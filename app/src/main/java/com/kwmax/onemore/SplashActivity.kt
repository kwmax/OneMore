package com.kwmax.onemore

import android.content.Intent
import android.widget.Toast
import com.kwmax.onemore.base.BaseActivity
import com.kwmax.onemore.base.delayLaunch
import kotlinx.coroutines.Dispatchers

/**
 * created by kwmax on 2020/1/8
 * desc:
 */
class SplashActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun initView() {
        delayLaunch(2000, context = Dispatchers.Main) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun initData() {}

    override fun needTransparentStatus(): Boolean = true
}
