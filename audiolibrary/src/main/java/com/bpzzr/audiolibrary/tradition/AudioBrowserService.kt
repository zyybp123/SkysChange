package com.bpzzr.audiolibrary.tradition

import android.app.Activity
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

class AudioBrowserService : MediaBrowserServiceCompat() {
    private val TAG = "AudioBrowserService"

    // 创建绑定
    private val binder = AudioBinder()
    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    companion object {
        /**
         * 静态启动服务
         */
        fun startAudioService(activity: Activity) {
            Intent(activity, AudioBrowserService::class.java).also { intent ->
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
        //创建媒体会话
        mediaSession = MediaSessionCompat(baseContext, TAG).apply {
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())

            // 接收媒体控制器回调
            setCallback(object : MediaSessionCompat.Callback() {

            })

            // Set the session's token so that client activities can communicate with it.
            setSessionToken(sessionToken)
        }

        LogUtil.e(TAG, "onCreate()")
        val notificationLayout = RemoteViews(
            this.packageName,
            R.layout.audio_lib_player_view_notification
        )
        val audioControlNotification = NotificationCompat.Builder(
            this, AudioFields.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.audio_icon_logo_note)
            .setContentTitle("测试通知")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            //.setStyle(NotificationCompat.DecoratedCustomViewStyle())
            //.setCustomContentView(notificationLayout)
            .build()
        // Notification ID cannot be 0.
        startForeground(AudioFields.AUDIO_CONTROL_ID, audioControlNotification)

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

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {

        // (Optional) Control the level of access for the specified package name.
        // You'll need to write your own logic to do this.
        /*return if (allowBrowsing(clientPackageName, clientUid)) {
            // Returns a root ID that clients can use with onLoadChildren() to retrieve
            // the content hierarchy.
            MediaBrowserServiceCompat.BrowserRoot(MY_MEDIA_ROOT_ID, null)
        } else {
            // Clients can connect, but this BrowserRoot is an empty hierachy
            // so onLoadChildren returns nothing. This disables the ability to browse for content.
            MediaBrowserServiceCompat.BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null)
        }*/
        return null
    }

    override fun onLoadChildren(
        parentMediaId: String,
        result: MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>>
    ) {
        //  Browsing not allowed
        /*if (MY_EMPTY_MEDIA_ROOT_ID == parentMediaId) {
            result.sendResult(null)
            return
        }

        // Assume for example that the music catalog is already loaded/cached.

        val mediaItems = emptyList<MediaBrowserCompat.MediaItem>()

        // Check if this is the root menu:
        if (MY_MEDIA_ROOT_ID == parentMediaId) {
            // Build the MediaItem objects for the top level,
            // and put them in the mediaItems list...
        } else {
            // Examine the passed parentMediaId to see which submenu we're at,
            // and put the children of that menu in the mediaItems list...
        }
        result.sendResult(mediaItems)*/
    }


    /**
     * 服务与界面绑定
     */
    inner class AudioBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): AudioBrowserService = this@AudioBrowserService
    }


}