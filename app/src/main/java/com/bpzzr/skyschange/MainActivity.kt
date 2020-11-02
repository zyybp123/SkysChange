package com.bpzzr.skyschange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bpzzr.audiolibrary.audio.AudioPlayer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_next).setOnClickListener { goNext() }

        //AudioPlayer.instance.startPlay()
    }

    private fun goNext() {
        startActivity(Intent(this, TestActivity::class.java))
        finish()
    }
}