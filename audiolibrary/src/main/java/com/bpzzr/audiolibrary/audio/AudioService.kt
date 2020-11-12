package com.bpzzr.audiolibrary.audio

import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.media.MediaBrowserServiceCompat
import com.bpzzr.audiolibrary.AudioFields
import com.bpzzr.audiolibrary.R
import com.bpzzr.commonlibrary.util.LogUtil

class AudioService : Service() {
    private val TAG = "AudioService"

    // 创建绑定
    private val binder = AudioBinder()

    //加载通知视图
    private val remoteViews = RemoteViews(
        this.packageName,
        R.layout.audio_lib_player_view_notification
    )

    //创建通知
    private val audioControlNotification = NotificationCompat.Builder(
        this, AudioFields.CHANNEL_ID
    )
        .setSmallIcon(R.drawable.audio_icon_logo_note)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setCustomBigContentView(remoteViews)
        .build()

    val audioPlayer: AudioPlayer = AudioPlayer.instance
    var audioPlaying: Boolean = false

    companion object {
        /**
         * 静态启动服务
         */
        fun startAudioService(activity: Activity) {
            Intent(activity, AudioService::class.java).also { intent ->
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    activity.startForegroundService(intent)
                } else {
                    activity.startService(intent)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        LogUtil.e(TAG, "onCreate()")
        remoteViews.setTextViewText(R.id.audio_tv_title, getString(R.string.audio_name_default))
        // 启动前台服务所需要的通知
        startForeground(AudioFields.AUDIO_CONTROL_ID, audioControlNotification)
        audioPlayer.setListener(object : AudioPlayer.Companion.AudioListener {
            override fun onStart() {

            }

            override fun onPrepared(duration: Int?) {
                audioPlaying = true
                remoteViews.setImageViewResource(
                    R.id.audio_iv_play,
                    android.R.drawable.ic_media_pause
                )
            }

            override fun onError() {

            }

            override fun onProgress(position: Int) {

            }
        })

        //初始化历史数据

        //设置封面点击跳转到音频详情页面
        remoteViews.setOnClickPendingIntent(
            R.id.audio_iv_cover,
            PendingIntent.getActivity(this, 12, Intent(), PendingIntent.FLAG_UPDATE_CURRENT)
        )
        //设置播放按钮点击
        //remoteViews.
        //设置上一首
        //设置下一首
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
        return binder
    }

    override fun onUnbind(intent: Intent): Boolean {
        // All clients have unbound with unbindService()
        return true
    }

    override fun onRebind(intent: Intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

    /**
     * 服务与界面绑定
     */
    inner class AudioBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): AudioService = this@AudioService
    }


}