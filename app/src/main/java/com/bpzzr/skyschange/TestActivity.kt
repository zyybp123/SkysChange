package com.bpzzr.skyschange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bpzzr.audiolibrary.audio.AudioPlayer

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        findViewById<TextView>(R.id.tv_start).setOnClickListener {
            AudioPlayer.instance.startPlay(this)
        }
        //AudioPlayer.instance.startPlay()
    }
}