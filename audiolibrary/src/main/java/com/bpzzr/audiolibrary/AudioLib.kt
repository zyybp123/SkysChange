package com.bpzzr.audiolibrary

import android.content.Context
import com.bpzzr.commonlibrary.NotificationUtil

class AudioLib {
    companion object {
        lateinit var context: Context

        fun init(context: Context) {
            this.context = context
            createNotify()
        }

        private fun createNotify() {
            NotificationUtil.createNotificationChannel(
                context, AudioFields.CHANNEL_NAME,
                AudioFields.CHANNEL_DESC, AudioFields.CHANNEL_ID
            )
        }
    }


}
