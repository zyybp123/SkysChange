package com.bpzzr.commonlibrary.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.bpzzr.commonlibrary.databinding.CbFragmentBaseBindingBinding

/**
 * BaseFragment的基类
 * 1、带顶部，底部容器，中间状态层
 * 2、基于ViewBinding
 *
 */
abstract class BaseBindingFragment<B : ViewBinding> : Fragment() {
    lateinit var mContext: Context
    lateinit var mActivity: Activity
    private var baseBinding: CbFragmentBaseBindingBinding? = null
    var hasResume: Boolean = false

    private var _successBinding: B? = null


    //对外用
    val mBinding get() = baseBinding!!
    val mSuccessBinding get() = _successBinding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (context is Activity) {
            mActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (baseBinding == null) {
            baseBinding = CbFragmentBaseBindingBinding.inflate(
                inflater, container, false
            )
        }
        val parent = mBinding.root.parent
        if (parent is ViewGroup) {
            parent.removeAllViews()
        }
        return mBinding.root
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paramsInitial(arguments)
        var sv = getSuccessView()
        if (sv == null) {
            sv = View.inflate(mContext, getSuccessViewId(), null)
        }
        if (_successBinding == null) {
            _successBinding = getSuccessBindingClass()
                .getMethod("bind", View::class.java)
                .invoke(null, sv) as B
            //添加成功的界面
            mBinding.cbStateLayout.addSuccessView(mSuccessBinding.root)
        }
    }

    inline fun <reified B : ViewBinding> getGenericType() = B::class.java

    fun getSuccessView(): View? {
        return null
    }

    @LayoutRes
    abstract fun getSuccessViewId(): Int

    inline fun <reified B : ViewBinding> inflate(view: View) {
        B::class.java.getMethod("bind", View::class.java)
            .invoke(null, view) as B
    }

    abstract fun getSuccessBindingClass(): Class<B>

    /**
     * 取参数
     */
    abstract fun paramsInitial(arguments: Bundle?)

    override fun onResume() {
        super.onResume()
        //只有第一次onResume的时候主动请求
        if (!hasResume) {
            hasResume = true
            dealRequest()
        }
        otherInOnResume()
    }

    fun otherInOnResume() {
        //可能需要在再次显示时要处理的
    }

    abstract fun dealRequest()

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onDestroy() {
        super.onDestroy()
        //在fragment被销毁时才彻底释放binding
        baseBinding = null
        _successBinding = null
    }
}