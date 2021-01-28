package com.bpzzr.audiolibrary.audio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bpzzr.audiolibrary.R

/**
 * 音频详情页面
 */
class AudioDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_detail)
        ViewModelProvider(this)
    }
}