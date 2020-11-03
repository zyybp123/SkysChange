package com.bpzzr.audiolibrary.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.TimedText
import android.net.Uri
import com.bpzzr.commonlibrary.LogUtil
import java.security.AccessControlContext

/**
 * 音频管理类
 */
class AudioPlayer private constructor() {

    companion object : MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnTimedTextListener {
        private lateinit var mediaPlayer: MediaPlayer
        private const val mTag = "AudioPlayer"

        val instance: AudioPlayer by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            //初始化播放器
            mediaPlayer = MediaPlayer()
            mediaPlayer.setOnPreparedListener(this)
            mediaPlayer.setOnCompletionListener(this)
            mediaPlayer.setOnErrorListener(this)
            mediaPlayer.setOnTimedTextListener(this)
            AudioPlayer()
        }


        override fun onPrepared(mediaPlayer: MediaPlayer?) {
            LogUtil.e(mTag, "onPrepared()")
            //播放器准备成功：可以调用相关获取的Api
            mediaPlayer?.start()
        }

        override fun onCompletion(p0: MediaPlayer?) {
            LogUtil.e(mTag, "OnCompletion()")
        }

        override fun onError(mediaPlayer: MediaPlayer?, what: Int, extra: Int): Boolean {
            LogUtil.e(mTag, "OnError($mediaPlayer,what=$what,extra=$extra)")
            return true
        }

        override fun onTimedText(mediaPlayer: MediaPlayer?, timedText: TimedText) {
            LogUtil.e(mTag, "onTimedText($timedText)")
        }

        /**
         * 记录当前播放器的状态
         */
        interface State {
            companion object {
                const val STATE_PREPARING = 0
                const val STATE_HAS_PREPARED = 1
                const val STATE_ON_PAUSE = 2
            }
        }


    }

    fun startPlay(context: Context) {
        mediaPlayer.reset()
        //设置播放资源
        mediaPlayer.setDataSource(
            context,
            Uri.parse("https://img.tukuppt.com/newpreview_music/09/00/94/5c89ac4ce85cb74039.mp3"),
            HashMap<String, String>()
        )
        //播放器异步准备
        mediaPlayer.prepareAsync()
        LogUtil.e(mTag, mediaPlayer)
    }

    private fun check(): Boolean {

        return false
    }

}