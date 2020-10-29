package com.bpzzr.audiolibrary.audio

import android.media.MediaPlayer
import com.bpzzr.commonlibrary.LogUtil

/**
 * 音频管理类
 */
class AudioPlayer private constructor() {
    private val mTag = "AudioPlayer"
    private val mediaPlayer = MediaPlayer()


    companion object {
        val instance: AudioPlayer by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AudioPlayer()
        }
    }

    fun startPlay() {
        //
        mediaPlayer.setDataSource("player path")
        LogUtil.e(mTag, mediaPlayer)
    }
}