package com.bpzzr.commonlibrary

import android.content.Context

class Common() {

    companion object {
        lateinit var mContext: Context

        const val DISK_CACHE_NAME = "appImageCache/"

        fun init(context: Context) {
            mContext = context
        }
    }
}