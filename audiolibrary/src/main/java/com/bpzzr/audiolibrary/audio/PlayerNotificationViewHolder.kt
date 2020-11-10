package com.bpzzr.audiolibrary.audio

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.bpzzr.audiolibrary.R

/**
 * 自定义播放控制的view
 */
class PlayerNotificationViewHolder {
    var mView: View
    var mIvCover: ImageView
    var mTvTitle: TextView
    var mTvSinger: TextView
    var mIvPrevious: ImageView
    var mIvPlay: ImageView
    var mIvNext: ImageView
    var mIvClose: ImageView

    constructor(context: Context) {
        this.mView = View.inflate(context, R.layout.audio_lib_player_view_notification, null)
        this.mIvCover = mView.findViewById(R.id.audio_iv_cover)
        this.mTvTitle = mView.findViewById(R.id.audio_tv_title)
        this.mTvSinger = mView.findViewById(R.id.audio_tv_singer)
        this.mIvPrevious = mView.findViewById(R.id.audio_iv_previous)
        this.mIvPlay = mView.findViewById(R.id.audio_iv_play)
        this.mIvNext = mView.findViewById(R.id.audio_iv_next)
        this.mIvClose = mView.findViewById(R.id.audio_iv_close)


    }
}