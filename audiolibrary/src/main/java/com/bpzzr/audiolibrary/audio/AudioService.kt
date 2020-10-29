package com.bpzzr.audiolibrary.audio

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.bpzzr.commonlibrary.LogUtil

/**
 * 音频播放服务
 */
const val TAG = "AudioService"

class AudioService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        //
        //AudioPlayer.instance.startPlay()
        //
        //AudioPlayer.instance.startPlay()
    }

    override fun onDestroy() {
        LogUtil.e(TAG, "onDestroy()")
        super.onDestroy()
    }

    companion object {
        private const val JOB_ID = 1000
        fun enqueueWork(context: Context?, work: Intent?) {
            enqueueWork(context!!, AudioService::class.java, JOB_ID, work!!)
        }
    }
}