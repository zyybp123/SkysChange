package com.bpzzr.commonlibrary.util

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.bpzzr.commonlibrary.base.BaseBindingFragment

inline fun <reified VB : ViewBinding> Activity.inflate() = lazy {
    inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}

/*inline fun <reified VB : ViewBinding> BaseBindingFragment.inflate() = lazy {
    var sv = getSuccessView()
    if (sv == null) {
        sv = View.inflate(mContext, getSuccessViewId(), null)
    }

    viewBinding<VB>(sv).apply {
        mBinding.cbStateLayout.addSuccessView(root)
    }
}*/

inline fun <reified VB : ViewBinding> Dialog.inflate() = lazy {
    inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified VB : ViewBinding> inflateBinding(
    layoutInflater: LayoutInflater
) = VB::class.java.getMethod("inflate", LayoutInflater::class.java)
    .invoke(null, layoutInflater) as VB


@Suppress("UNCHECKED_CAST")
inline fun <reified VB : ViewBinding> viewBinding(
    view: View?
) = VB::class.java.getMethod("bind", View::class.java)
    .invoke(null, view!!) as VB
