package com.bpzzr.audiolibrary.tradition

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

private const val MY_MEDIA_ROOT_ID = "media_root_id"
private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"

/**
 * 媒体播放服务
 *
 */
class AudioBrowserService : MediaBrowserServiceCompat() {
    private val TAG = "AudioBrowserService"

    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    companion object {

    }

    override fun onCreate() {
        super.onCreate()

        // Create a MediaSessionCompat
        mediaSession = MediaSessionCompat(baseContext, TAG).apply {
            //初始播放状态
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())
            // 设置回调
            setCallback(object : MediaSessionCompat.Callback() {

            })
            // 设置会话的令牌，以便客户端活动可以与其通信
            setSessionToken(sessionToken)
            //

        }
        //
    }

    override fun onLoadChildren(
        parentMediaId: String,
        result: MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>>
    ) {
        // 不可浏览
        if (MY_EMPTY_MEDIA_ROOT_ID == parentMediaId) {
            result.sendResult(null)
            return
        }

        // Assume for example that the music catalog is already loaded/cached.

        /*val mediaItems = emptyList<MediaBrowserCompat.MediaItem>()

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

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        //根据端做判断
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
}