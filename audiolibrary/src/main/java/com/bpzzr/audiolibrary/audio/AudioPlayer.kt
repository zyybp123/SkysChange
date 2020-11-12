package com.bpzzr.audiolibrary.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.bpzzr.commonlibrary.util.LogUtil
import com.bpzzr.commonlibrary.util.PeriodicUtil

/**
 * 音频管理类
 */
class AudioPlayer private constructor() {

    companion object : MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnSeekCompleteListener, PeriodicUtil.Task {
        private lateinit var mediaPlayer: MediaPlayer
        private lateinit var periodicUtil: PeriodicUtil
        private const val mTag = "AudioPlayer"
        private var mState: Int = State.STATE_PREPARING
        private var mAudioListener: AudioListener? = null

        val instance: AudioPlayer by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            //初始化播放器
            mediaPlayer = MediaPlayer()
            mediaPlayer.setOnPreparedListener(this)
            mediaPlayer.setOnCompletionListener(this)
            mediaPlayer.setOnErrorListener(this)
            mediaPlayer.setOnBufferingUpdateListener(this)
            mediaPlayer.setOnSeekCompleteListener(this)
            periodicUtil = PeriodicUtil(this, 0, 1000)
            AudioPlayer()
        }


        override fun onPrepared(mediaPlayer: MediaPlayer?) {
            LogUtil.e(mTag, "onPrepared()")
            //播放器准备成功：可以调用相关获取的Api
            mState = State.STATE_HAS_PREPARED
            mediaPlayer?.start()
            periodicUtil.start()
            mAudioListener?.onPrepared(mediaPlayer?.duration)
        }

        override fun onCompletion(mediaPlayer: MediaPlayer?) {
            LogUtil.e(mTag, "OnCompletion()")
            //播放已经完成
            mState = State.STATE_HAS_COMPLETED
        }

        override fun onError(mediaPlayer: MediaPlayer?, what: Int, extra: Int): Boolean {
            LogUtil.e(mTag, "OnError($mediaPlayer,what=$what,extra=$extra)")
            //b播放中出错，应该抛出
            mAudioListener?.onError()
            return true
        }

        override fun onBufferingUpdate(mediaPlayer: MediaPlayer?, percent: Int) {
            LogUtil.e(mTag, "onBufferingUpdate($percent)")
        }

        override fun onSeekComplete(mediaPlayer: MediaPlayer?) {
            //seekTo(int)方法是异步执行的，所以它可以马上返回，
            // 但是实际的定位播放操作可能需要一段时间才能完成，尤其是播放流形式的音频/视频。
            // 当实际的定位播放操作完成之后会进入此接口完成回调
            LogUtil.e(mTag, "onSeekComplete($mediaPlayer)")
        }


        /**
         * 记录当前播放器的状态
         * 准备中，已经准备完成，
         */
        interface State {
            companion object {
                const val STATE_PREPARING = 0
                const val STATE_HAS_PREPARED = 1
                const val STATE_ON_PAUSE = 2
                const val STATE_HAS_COMPLETED = 3
                const val STATE_RELEASE = 4
            }
        }

        interface AudioListener {
            fun onStart()
            fun onPrepared(duration: Int?)
            fun onError()
            fun onProgress(position: Int)
        }

        override fun execute() {
            //执行周期任务，将进度传出
            mAudioListener?.onProgress(mediaPlayer.currentPosition)
        }

    }

    fun setListener(listener: AudioListener) {
        mAudioListener = listener
    }

    fun checkIsPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun pause() {
        mState = State.STATE_ON_PAUSE
        mediaPlayer.pause()
    }

    fun startPlay(context: Context) {
        mState = State.STATE_PREPARING
        mAudioListener?.onStart()
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

    fun release() {
        mState = State.STATE_RELEASE
        mediaPlayer.release()
    }

    fun getState(): Int {
        return mState
    }

    fun getDuration(): Int {
        return mediaPlayer.duration
    }


    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun seekTo(progress: Int) {
        mediaPlayer.seekTo(progress)
    }


}