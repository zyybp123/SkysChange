package com.bpzzr.skyschange

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.bpzzr.audiolibrary.AudioLib
import com.bpzzr.commonlibrary.util.LogUtil
import com.bpzzr.managerlib.ManagerLib
import com.bpzzr.managerlib.rong.BaseConversationActivity

class SkyChange : Application() {
    companion object {
        lateinit var mContext: Context
    }


    override fun onCreate() {
        super.onCreate()
        mContext = this

        AudioLib.init(this)

        ManagerLib.init(this)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is BaseConversationActivity){
                    activity.setTheme(R.style.AppThemeDark)
                }
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
                LogUtil.e("$activity onActivityStarted()")
            }

            override fun onActivityResumed(activity: Activity) {
                LogUtil.e("$activity onActivityResumed()")
            }

            override fun onActivityPaused(activity: Activity) {
                LogUtil.e("$activity onActivityPaused()")
            }

            override fun onActivityStopped(activity: Activity) {
                LogUtil.e("$activity onActivityStopped()")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                LogUtil.e("$activity onActivitySaveInstanceState()")
            }

            override fun onActivityDestroyed(activity: Activity) {
                LogUtil.e("$activity onActivityDestroyed()")
            }
        })
    }
}