package com.bpzzr.commonlibrary.widget

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.bpzzr.commonlibrary.util.LogUtil

class FloatWidget(activity: Activity, view: View) {
    private val mTag = "FloatWidget"
    private lateinit var contentView: View

    init {
        try {
            contentView =
                activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
            (contentView as ViewGroup?)?.addView(
                view, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        } catch (ex: Exception) {
            LogUtil.e(mTag, "init error: $ex")
        }
    }

    companion object {
        fun create(activity: Activity, view: View): FloatWidget {
            return FloatWidget(activity, view)
        }
    }


}