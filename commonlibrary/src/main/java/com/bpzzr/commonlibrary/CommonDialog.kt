package com.bpzzr.commonlibrary

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bpzzr.commonlibrary.util.DimensionUtil
import com.bpzzr.commonlibrary.util.LogUtil

/**
 * 封装通用对话框组件
 * 基本对话框为包含头、中、底三个容器的界面
 * 其余种类的对话框则在此基础上扩展
 */
class CommonDialog(
    private var activity: Activity,
    private var widthMargin: Int = 60,
    private var sePadding: Int? = null,
    private var tbPadding: Int? = null,
    private var canCancel: Boolean = true,
    private var canCancelOut: Boolean = false
) : LifecycleObserver {
    private val rootView: View = View.inflate(activity, R.layout.cb_base_dailog, null)
    private val flHeader: FrameLayout
    private val flContent: FrameLayout
    private val llBottom: LinearLayout
    private var dialog: Dialog? = null
    private val mTag = "CommonDialog"

    init {
        if (sePadding == null) {
            sePadding = DimensionUtil.getPxSize(activity, R.dimen.dp_30)
        }
        if (tbPadding == null) {
            tbPadding = DimensionUtil.getPxSize(activity, R.dimen.dp_12)
        }
        rootView.setPadding(sePadding!!, tbPadding!!, sePadding!!, tbPadding!!)
        flHeader = rootView.findViewById(R.id.cb_dialog_header)
        flContent = rootView.findViewById(R.id.cb_dialog_content)
        llBottom = rootView.findViewById(R.id.cb_dialog_bottom)
        (activity as AppCompatActivity).lifecycle.addObserver(this)
    }

    /**
     * 构建普通的文本提示对话框
     * @param title 对话框标题 （文本高度为40dp）
     * @param desc 对话框的描述文本(最小值120dp，最大值屏幕高的2/3)
     * @param sureText 底部确定按钮文字 （文本高度为40dp）
     * @param getter 监听器，主要用于获取控件来自定义样式
     */
    fun buildDefaultTipDialog(
        title: String = "",
        desc: String = "",
        sureText: String = activity.getString(R.string.cb_sure),
        getter: DefaultTipDialog? = null
    ): CommonDialog {
        //构建标题
        val tvTitle = createText(title)
        tvTitle.textSize = 18F
        flHeader.addView(tvTitle, getTextHeightParams())
        //构建中间描述文本
        val tvContent = with(createText(desc)) {
            movementMethod = ScrollingMovementMethod.getInstance()
            isVerticalScrollBarEnabled = true
            maxHeight = DimensionUtil.totalSize(activity).x / 3 * 2
            minHeight = DimensionUtil.getPxSize(activity, R.dimen.dp_120)
            gravity = Gravity.NO_GRAVITY
            setTextColor(ContextCompat.getColor(activity, R.color.cb_color_999))
            setLineSpacing(0F, 1.2F)
            this
        }
        flContent.addView(tvContent, getHeightWrapParams())
        //构建底部按钮
        val tvSure = createText(sureText)
        tvSure.setTextColor(ContextCompat.getColor(activity, R.color.cb_color_dialog_sure))
        llBottom.gravity = Gravity.END or Gravity.CENTER_VERTICAL
        llBottom.addView(tvSure, getTextHeightParamsWrap())
        tvSure.setOnClickListener { dismiss() }
        constructDialog()
        //将构建元素带出用来自定义
        getter?.elementGet(tvTitle, tvSure, dialog)
        return this
    }


    fun buildEditDialog() {
        val editText = EditText(activity)
    }

    private fun getTextHeightParamsWrap(): ViewGroup.LayoutParams {
        return ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            DimensionUtil.getPxSize(activity, R.dimen.dp_40)
        )
    }

    private fun getTextHeightParams(): ViewGroup.LayoutParams {
        return ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            DimensionUtil.getPxSize(activity, R.dimen.dp_40)
        )
    }

    private fun getFullParams(): ViewGroup.LayoutParams {
        return ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }

    private fun getHeightWrapParams(): ViewGroup.LayoutParams {
        return ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    fun createText(title: String): TextView {
        return with(TextView(activity)) {
            textSize = 16F
            setTextColor(ContextCompat.getColor(activity, R.color.cb_color_333))
            text = title
            gravity = Gravity.CENTER_VERTICAL
            this
        }
    }

    private fun constructDialog(): Dialog? {
        dialog = Dialog(activity)
        //配置view
        dialog?.setContentView(
            rootView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        )
        //配置窗口信息
        val window: Window? = dialog?.window
        val params = window?.attributes
        //（Dialog依附于windowManager）
        params?.gravity = Gravity.CENTER
        params?.width = DimensionUtil.totalSize(activity).x - widthMargin * 2
        window?.attributes = params

        dialog?.setCancelable(canCancel)
        dialog?.setCanceledOnTouchOutside(canCancelOut)
        return dialog
    }

    fun show() {
        if (!activity.isFinishing && !activity.isDestroyed)
            dialog?.show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        LogUtil.e(mTag, "$activity call onDestroy()")
        dismiss()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    interface DefaultTipDialog {
        fun elementGet(tvTitle: TextView, tvSure: TextView, dialog: Dialog?)
    }
}