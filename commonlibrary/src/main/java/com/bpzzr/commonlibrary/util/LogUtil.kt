package com.bpzzr.commonlibrary.util

import android.util.Log
import com.bpzzr.commonlibrary.BuildConfig

class LogUtil private constructor(){

    companion object {
        const val TAG = "LogUtil"
        private val isShowLog = BuildConfig.DEBUG

        fun v(tag: String, msg: String) {
            if (isShowLog)
                Log.v(tag, msg)
        }

        fun d(tag: String, msg: String) {
            if (isShowLog)
                Log.d(tag, msg)
        }

        fun i(tag: String, msg: String) {
            if (isShowLog)
                Log.i(tag, msg)
        }

        fun w(tag: String, msg: String) {
            if (isShowLog)
                Log.w(tag, msg)
        }

        fun e(msg: String) {
            e(TAG, msg)
        }

        fun e(tag: String, msg: String) {
            if (isShowLog)
                Log.e(tag, msg)
        }

        fun e(tag: String, obj: Any) {
            e(tag, obj.toString())
        }
    }
}