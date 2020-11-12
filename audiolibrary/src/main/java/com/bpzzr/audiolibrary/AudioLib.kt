package com.bpzzr.audiolibrary

import android.content.Context
import com.bpzzr.commonlibrary.util.NotificationUtil

class AudioLib {
    companion object {
        lateinit var context: Context

        fun init(context: Context) {
            this.context = context
            createNotify()
        }

        private fun createNotify() {
            NotificationUtil.createNotificationChannel(
                context, context.getString(R.string.audio_channel_name),
                context.getString(R.string.audio_channel_desc), AudioFields.CHANNEL_ID
            )
        }
    }


}
