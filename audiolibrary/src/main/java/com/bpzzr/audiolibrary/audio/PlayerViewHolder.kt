package com.bpzzr.audiolibrary.audio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.bpzzr.audiolibrary.R

/**
 * 音频播放器的控制界面
 *
 */
class PlayerViewHolder {

    var mView: View
    var mIvCover: ImageView
    var mTvTitle: TextView
    var mIvPrevious: ImageView
    var mIvPlay: ImageView
    var mIvNext: ImageView
    var mTvCurrent: TextView
    var mTvAll: TextView
    var mSeekBar: SeekBar

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

}