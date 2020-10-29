package com.bpzzr.skyschange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bpzzr.audiolibrary.audio.AudioPlayer

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        AudioPlayer.instance.startPlay()
    }
}