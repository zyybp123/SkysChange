package com.bpzzr.skyschange

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bpzzr.commonlibrary.util.LogUtil
import kotlinx.coroutines.delay

class TestFragment : Fragment() {
    private val mTag = "TestFragment"
    private var rootView: View? = null
    private lateinit var tvTest: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.e(mTag,"onCreate()")
        lifecycleScope.launchWhenResumed {
            LogUtil.e(mTag, "在onResumed时执行")
            tvTest.text = "在onResumed时执行"

            delay(3000)
            LogUtil.e(mTag, "is over end")
            tvTest.text = "12345"
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_test, null)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTest = view.findViewById(R.id.tv_test)
    }

}