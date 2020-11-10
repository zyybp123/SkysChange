package com.bpzzr.commonlibrary

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.DialogCompat
import androidx.core.content.ContextCompat
import java.util.*

/**
 * 动态权限申请处理类
 *
 */
class DynamicPermission(
    private var activity: AppCompatActivity,
    private var requestPermissions: Array<String>,
    private var listener: OnPermissionListener?
) {
    private val mActivityLauncher: ActivityResultLauncher<Array<String>>
    private val mPermissionLauncher: ActivityResultLauncher<String>
    private val mTag = "DynamicPermission"

    init {
        mPermissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGrant: Boolean? ->
                LogUtil.e(mTag, "isGrant:$isGrant")
            }
        mActivityLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: Map<String, Boolean>? ->
            LogUtil.e(mTag, "result:$result")
            //判断需要请求的权限是否均通过
            result?.keys?.forEach { name ->
                if (result[name] == false) {
                    //代表某项权限被拒绝了
                    if (listener?.onPermissionDenied(name) == false) {
                        //回调为true时不会弹出默认的吐司
                        Toast.makeText(
                            activity.applicationContext,
                            "您拒绝了{$name}权限的申请", Toast.LENGTH_LONG
                        ).show()
                    }
                    return@registerForActivityResult
                }
            }
            listener?.onPermissionPass()
        }
        onCreate()
    }

    companion object {

        @RequiresApi(Build.VERSION_CODES.M)
        fun isOverlays(context: Context): Boolean {
            return Settings.canDrawOverlays(context)
        }
    }

    private fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查所有权限
            val permissions: Array<MutableList<String>> = getPermissions(*requestPermissions)
            LogUtil.e(mTag, "permissions:${permissions[0]},${permissions[1]}")
            when {

                permissions[1].isNotEmpty() -> {
                    //被拒绝后不再提示的权限组,需要手动去设置页
                    val d = Dialog(activity);
                    d.setTitle("帮助")
                    d.show()
                }
                else -> {
                    mActivityLauncher.launch(permissions[0].toTypedArray())
                }

            }
        } else {
            listener?.onPermissionPass()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPermissions(vararg permission: String): Array<MutableList<String>> {
        val permissions: MutableList<String> = ArrayList()
        val permissionsRa: MutableList<String> = ArrayList()
        for (aPermission in permission) {
            val checkSelfPermission = ContextCompat.checkSelfPermission(
                activity,
                aPermission
            )
            if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
                //没有的权限，将其加入权限集合
                permissions.add(aPermission)
            }
            if (!activity.shouldShowRequestPermissionRationale(aPermission)) {
                //用户同时点了拒绝，且不再提醒时的权限组
                permissionsRa.add(aPermission)
            }
        }
        return arrayOf(permissions, permissionsRa)
    }


    interface OnPermissionListener {
        fun onPermissionPass()
        fun onPermissionDenied(permission: String?): Boolean
    }


}