package com.kwmax.onemore.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 * created by kwmax on 2020/1/8
 * desc:
 */
abstract class BaseActivity : AppCompatActivity(),EasyPermissions.PermissionCallbacks,
    CoroutineScope by MainScope() {

    companion object{
        const val PERMISSION_CODE = 0X01
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (needTransparentStatus()) transparentStatusBar()
        setContentView(getLayoutId())
        initView()
        initData()
    }

    abstract fun getLayoutId(): Int
    abstract fun initView()
    open fun initData() {}

    /** 透明状态栏 */
    open fun transparentStatusBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
        supportActionBar?.hide()
    }

    protected open fun needTransparentStatus(): Boolean = false

    protected fun startActivity(z : Class<*>){
        startActivity(Intent(applicationContext,z))
    }

    protected fun showToast(msg:String){
        Toast.makeText(applicationContext,msg,Toast.LENGTH_SHORT).show()
    }

    //请求一些必须要的权限
    protected fun requestPermission(permission : Array<String>) {
        if (EasyPermissions.hasPermissions(this, *permission)) {
            //具备权限 直接进行操作
            onPermissionSuccess()
        } else {
            //权限拒绝 申请权限
            EasyPermissions.requestPermissions(this, "为了正常使用，需要获取以下权限", PERMISSION_CODE, *permission); }
    }

    //权限申请相关
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //权限获取成功
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        onPermissionSuccess()
    }

    //权限获取被拒绝
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        /**
         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //拒绝了权限，而且选择了不在提醒，需要去手动设置了
            AppSettingsDialog.Builder(this).build().show()
        }
        //拒绝了权限，重新申请
        else{
            onPermissionFail()
        }
    }

    /**
     * 权限申请成功执行方法
     */
    protected open fun onPermissionSuccess(){

    }
    /**
     * 权限申请失败
     */
    protected open fun onPermissionFail(){

    }

}