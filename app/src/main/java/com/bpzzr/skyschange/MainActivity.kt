package com.bpzzr.skyschange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bpzzr.audiolibrary.audio.AudioPlayer
import com.bpzzr.managerlib.ManagerLib

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_next).setOnClickListener { goNext() }
        ManagerLib.connectRong(
            "alKvTb24v6CzwdQ1Mlq4n5BZn+T8TlRxXOa/j1rM422AMWMFQRoeKw==@pjv5.cn.rongnav.com;pjv5.cn.rongcfg.com")
        //AudioPlayer.instance.startPlay()
    }

    private fun goNext() {
        startActivity(Intent(this, TestActivity::class.java))
        finish()
    }
}