package com.bpzzr.commonlibrary

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.TypedArray
import android.os.Build
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bpzzr.commonlibrary.util.LogUtil
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
    private val mDialog: CommonDialog

    init {
        mPermissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGrant: Boolean? ->
                LogUtil.e(mTag, "isGrant:$isGrant")
            }
        mDialog = CommonDialog(activity)
        mActivityLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: Map<String, Boolean>? ->
            LogUtil.e(mTag, "result:$result")
            //判断需要请求的权限是否均通过
            result?.keys?.forEach { name ->
                if (result[name] == false) {
                    //代表某项权限被拒绝了
                    if (listener?.onPermissionDenied(name) == false) {
                        //回调为true时不会弹出默认的弹窗
                        mDialog.buildDefaultTipDialog(
                            activity.resources.getString(R.string.cb_help),
                            "您拒绝了${getNameChinese(name)}权限的申请，可能会影响应用的正常使用",
                            activity.resources.getString(R.string.cb_permission_i_knowed),
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

        @SuppressLint("QueryPermissionsNeeded")
        fun openSettings(activity: AppCompatActivity) {
            val intent = Intent(Settings.ACTION_SETTINGS)
            if (intent.resolveActivity(activity.packageManager) != null) {
                activity.startActivity(intent)
            }
        }

        fun getNameChinese(permission: String): String {
            return when {
                Manifest.permission.CAMERA == permission -> {
                    "相机"
                }
                Manifest.permission.READ_EXTERNAL_STORAGE == permission ||
                        Manifest.permission.WRITE_EXTERNAL_STORAGE == permission -> {
                    "存储空间"
                }
                Manifest.permission.ACCESS_COARSE_LOCATION == permission -> {
                    "地理位置"
                }
                Manifest.permission.READ_PHONE_STATE == permission -> {
                    "手机\\电话"
                }
                else -> {
                    ""
                }
            }
        }
    }

    private fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查所有权限
            val permissions: MutableList<String> = getPermissions(*requestPermissions)
            LogUtil.e(mTag, "permissions:${permissions[0]},${permissions[1]}")
            //所需权限全部通过
            if (permissions[0].isEmpty()) {
                listener?.onPermissionPass()
                return
            }
            //申请被拒权限
            mActivityLauncher.launch(permissions.toTypedArray())
        } else {
            listener?.onPermissionPass()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPermissions(vararg permission: String): MutableList<String> {
        val permissions: MutableList<String> = ArrayList()
        for (aPermission in permission) {
            val checkSelfPermission = ContextCompat.checkSelfPermission(
                activity,
                aPermission
            )
            LogUtil.e(mTag, "should: ${activity.shouldShowRequestPermissionRationale(aPermission)}")
            if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
                //没有的权限，将其加入权限集合
                permissions.add(aPermission)
            }
        }
        return permissions
    }


    interface OnPermissionListener {
        fun onPermissionPass()
        fun onPermissionDenied(permission: String?): Boolean
    }


}