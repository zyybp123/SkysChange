package com.bpzzr.commonlibrary.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bpzzr.commonlibrary.R

/**
 * Activity基类，用于封装统一的操作处理
 */
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}