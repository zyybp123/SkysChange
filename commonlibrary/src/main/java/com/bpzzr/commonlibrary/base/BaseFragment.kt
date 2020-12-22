package com.bpzzr.commonlibrary.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bpzzr.commonlibrary.R
import com.bpzzr.commonlibrary.util.LogUtil
import com.bpzzr.commonlibrary.widget.StateLayout

/**
 *Fragment基类
 */
abstract class BaseFragment : Fragment() {
    private val mTag = this.javaClass.name
    private lateinit var mContext: Context
    private lateinit var mActivity: Activity
    var mRootView: View? = null
    var mHeaderView: FrameLayout? = null
    var mFooterView: FrameLayout? = null
    var mStateLayout: StateLayout? = null
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var successView: View? = null

    //默认采用懒加载，即已经经过resume的页面不在重复数据请求
    var isLazyLoad: Boolean = true
    private var mIsResumed: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LogUtil.e(mTag, "onAttach()")
        mContext = context
        if (context is Activity) {
            mActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.e(mTag, "onCreate()")
        loadLaunchParams(arguments)
    }

    /**
     * 加载启动参数
     */
    abstract fun loadLaunchParams(arguments: Bundle?)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.cb_fragment_base, null)
        }
        mHeaderView = mRootView?.findViewById(R.id.cb_fl_header)
        mFooterView = mRootView?.findViewById(R.id.cb_fl_footer)
        mStateLayout = mRootView?.findViewById(R.id.cb_state_layout)
        mSwipeRefreshLayout = mRootView?.findViewById(R.id.cb_swipe_refresh_layout)
        val parent = mRootView?.parent
        if (parent is ViewGroup) {
            parent.removeAllViews()
        }
        //获取成功的页面
        successView = getSuccessView()
        if (successView == null) {
            successView = View.inflate(mContext, getSuccessViewLayoutId(), null)
        }
        mStateLayout?.addSuccessView(successView!!)
        LogUtil.e(mTag, "onCreateView(rootView:$mRootView)")
        return mRootView
    }

    /**
     * 子类如果复写此方法，则用该view
     */
    fun getSuccessView(): View? {
        return null
    }

    /**
     * 子类优先使用此方法加载成功布局
     */
    abstract fun getSuccessViewLayoutId(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUtil.e(mTag, "onViewCreated()")
        viewBind(view, savedInstanceState)
    }

    /**
     * 子类重写此方法绑定成功视图的控件
     */
    abstract fun viewBind(view: View, savedInstanceState: Bundle?)

    override fun onStart() {
        super.onStart()
        LogUtil.e(mTag, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        LogUtil.e(mTag, "onResume()")
        if (!mIsResumed) {
            mIsResumed = true
            if (isLazyLoad) {
                requestData()
            }
        }
        if (!isLazyLoad){
            requestData()
        }
    }

    /**
     * 子类实现后，在此请求数据
     */
    abstract fun requestData()

    override fun onPause() {
        super.onPause()
        LogUtil.e(mTag, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        LogUtil.e(mTag, "onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.e(mTag, "onDestroyView()")
    }

    override fun onDestroy() {
        LogUtil.e(mTag, "onDestroy()")
        super.onDestroy()
    }

    override fun onDetach() {
        LogUtil.e(mTag, "onDetach()")
        super.onDetach()
    }
}