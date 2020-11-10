package com.bpzzr.skyschange

import android.app.Application
import android.content.Context
import com.bpzzr.audiolibrary.AudioLib

class SkyChange : Application() {
    companion object {
        lateinit var mContext: Context
    }


    override fun onCreate() {
        super.onCreate()
        mContext = this

        AudioLib.init(this)
    }
}