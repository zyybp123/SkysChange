package com.bpzzr.commonlibrary.base

import android.os.Bundle
import com.bpzzr.commonlibrary.R

class BaseContainerActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cb_activity_base_container)
    }
}