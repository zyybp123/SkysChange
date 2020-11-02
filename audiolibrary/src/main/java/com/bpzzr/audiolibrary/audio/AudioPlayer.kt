package com.bpzzr.audiolibrary.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.bpzzr.commonlibrary.LogUtil
import java.security.AccessControlContext

/**
 * 音频管理类
 */
class AudioPlayer private constructor() {

    companion object : MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {
        private lateinit var mediaPlayer: MediaPlayer
        private const val mTag = "AudioPlayer"

        val instance: AudioPlayer by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            //初始化播放器
            mediaPlayer = MediaPlayer()
            mediaPlayer.setOnPreparedListener(this)
            mediaPlayer.setOnCompletionListener(this)
            mediaPlayer.setOnErrorListener(this)
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
    }

    fun startPlay(context: Context) {
        mediaPlayer.reset()
        //设置播放资源
        mediaPlayer.setDataSource(
            context,
            Uri.parse("https://audio04.dmhmusic.com/71_53_T10040588985_128_4_1_0_sdk-cpm/cn/0311/M00/41/A4/ChAKDF1vh4mAUHwzAD5uruuB28o022.mp3?xcode=2e10cd92770c989c738035eff55994f44ab6f2e"),
            HashMap<String, String>()
        )
        //播放器异步准备
        mediaPlayer.prepareAsync()
        //mediaPlayer.prepare()
        //mediaPlayer.start()
        LogUtil.e(mTag, mediaPlayer)
    }

    private fun check(): Boolean {

        return false
    }

}