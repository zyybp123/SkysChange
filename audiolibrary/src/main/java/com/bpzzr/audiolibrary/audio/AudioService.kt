package com.bpzzr.audiolibrary.audio

import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.JobIntentService
import androidx.lifecycle.ViewModelProvider
import com.bpzzr.commonlibrary.LogUtil

class AudioService : Service() {
    private val TAG = "AudioService"


    companion object {
        fun startAudioService(pendingIntent: PendingIntent, activity: Activity) {
           /* val notification: Notification = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Notification.Builder(activity, AUDIO_CHANNEL)
                    .setContentTitle(getText(R.string.notification_title))
                    .setContentText(getText(R.string.notification_message))
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(pendingIntent)
                    .setTicker(getText(R.string.ticker_text))
                    .build()
            } else {
                Notification.Builder(activity)
                    .setContentTitle(getText(R.string.notification_title))
                    .setContentText(getText(R.string.notification_message))
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(pendingIntent)
                    .setTicker(getText(R.string.ticker_text))
                    .build()
            }

            // Notification ID cannot be 0.
            startForeground(ONGOING_NOTIFICATION_ID, notification)*/
        }

    }

    override fun onCreate() {
        super.onCreate()
        LogUtil.e(TAG, "onStartCommand()")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtil.e(TAG, "onStartCommand()")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        LogUtil.e(TAG, "onDestroy()")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent): Boolean {
        // All clients have unbound with unbindService()
        return true
    }

    override fun onRebind(intent: Intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

}