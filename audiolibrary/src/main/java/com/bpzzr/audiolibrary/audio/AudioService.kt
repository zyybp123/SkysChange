package com.bpzzr.audiolibrary.audio

import android.app.Activity
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import com.bpzzr.audiolibrary.AudioFields
import com.bpzzr.audiolibrary.R
import com.bpzzr.commonlibrary.util.LogUtil

class AudioService : Service(), AudioPlayer.Companion.AudioListener, LifecycleOwner {
    private val mTag = "AudioService"

    private var remoteViews: RemoteViews? = null
    private var audioControlNotification: Notification? = null

    private var playerStateObserver: MutableLiveData<AudioControlEntity> = MutableLiveData()


    private val audioPlayer: AudioPlayer = AudioPlayer.instance

    private val mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    var audioPlaying: Boolean = false

    companion object {
        val playerCommandReceiver: MutableLiveData<AudioControlEntity> = MutableLiveData()

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
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        LogUtil.e(mTag, "onCreate()")
        remoteViews = RemoteViews(
            this.packageName,
            R.layout.audio_lib_player_view_notification
        )
        audioControlNotification = NotificationCompat.Builder(
            this, AudioFields.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.audio_icon_logo_note)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCustomBigContentView(remoteViews)
            .build()

        startForeground(AudioFields.AUDIO_CONTROL_ID, audioControlNotification)
        audioPlayer.setListener(this)
        playerCommandReceiver.observe(
            this, { audioControlEntity ->
                when {
                    ControlCommand.PLAY == audioControlEntity?.cammand -> {
                        //开始播放
                        audioPlayer.startPlay(
                            this@AudioService,
                            "https://img.tukuppt.com/newpreview_music/09/00/94/5c89ac4ce85cb74039.mp3"
                        )
                    }
                    ControlCommand.PAUSE == audioControlEntity?.cammand -> {
                        //暂停
                        audioPlayer.pause()
                    }
                    ControlCommand.NEXT == audioControlEntity?.cammand -> {
                        //下一首
                        audioPlayer.startPlay(this@AudioService, "")
                    }
                    ControlCommand.PREVIOUS == audioControlEntity?.cammand -> {
                        //上一首
                        audioPlayer.startPlay(this@AudioService, "")
                    }

                }
            })
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtil.e(mTag, "onStartCommand()")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        LogUtil.e(mTag, "onDestroy()")
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStart() {
        //界面要显示加载中
        playerStateObserver.postValue(AudioControlEntity(ControlCommand.START))
    }

    override fun onPrepared(duration: Int?) {
        //界面显示总时长
        playerStateObserver.postValue(AudioControlEntity(ControlCommand.PREPARED, duration))
    }

    override fun onError(mediaPlayer: MediaPlayer?, what: Int, extra: Int) {
        //界面反馈错误信息
        playerStateObserver.postValue(
            AudioControlEntity(
                cammand = ControlCommand.ERROR, errorWhat = "what:$what,extra:$extra"
            )
        )
    }

    override fun onProgress(position: Int) {
        //界面反馈进度信息
        playerStateObserver.postValue(
            AudioControlEntity(
                cammand = ControlCommand.PROGRESS, progress = position
            )
        )
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }


}