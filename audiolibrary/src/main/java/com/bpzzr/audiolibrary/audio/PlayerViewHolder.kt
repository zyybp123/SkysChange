package com.bpzzr.audiolibrary.audio

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.bpzzr.audiolibrary.R
import com.bpzzr.commonlibrary.util.LogUtil

/**
 * 音频播放器的控制界面
 *
 */
class PlayerViewHolder {
    private val mTag = "PlayerViewHolder"
    var mView: View
    var mIvCover: ImageView
    var mTvTitle: TextView
    var mIvPrevious: ImageView
    var mIvPlay: ImageView
    var mIvNext: ImageView
    var mTvCurrent: TextView
    var mTvAll: TextView
    var mSeekBar: SeekBar
    private lateinit var mService: AudioService
    private var mBound: Boolean = false
    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // Called when the connection with the service is established
            LogUtil.e(mTag, "onServiceDisconnected")
            //val binder = service as AudioService.AudioBinder
            //mService = binder.getService()
            mBound = true

        }


        override fun onServiceDisconnected(className: ComponentName) {
            // 当与服务的连接意外中断（例如服务崩溃或被终止）时，Android 系统会调用该方法。
            // 当客户端取消绑定时，系统不会调用该方法
            LogUtil.e(mTag, "onServiceDisconnected")
            mBound = false
        }
    }

    constructor(context: Context) {
        this.mView = View.inflate(context, R.layout.audio_lib_player_view_small, null)
        this.mIvCover = mView.findViewById(R.id.audio_iv_cover)
        this.mTvTitle = mView.findViewById(R.id.audio_tv_title)
        this.mIvPrevious = mView.findViewById(R.id.audio_iv_previous)
        this.mIvPlay = mView.findViewById(R.id.audio_iv_play)
        this.mIvNext = mView.findViewById(R.id.audio_iv_next)
        this.mTvCurrent = mView.findViewById(R.id.audio_tv_current)
        this.mTvAll = mView.findViewById(R.id.audio_tv_all)
        this.mSeekBar = mView.findViewById(R.id.audio_seek_bar)


    }

    /**
     * 在activity的onStart里调用绑定
     */
    fun onStart(activity: Activity) {
        Intent(activity, AudioService::class.java).also { intent ->
            activity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }

    }

    /**
     * 在activity的onStop里调用解绑
     */
    fun onStop(activity: Activity) {
        activity.unbindService(mConnection)
        mBound = false
    }

}