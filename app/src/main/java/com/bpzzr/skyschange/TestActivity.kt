package com.bpzzr.skyschange

import android.Manifest
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bpzzr.audiolibrary.audio.AudioPlayer
import com.bpzzr.audiolibrary.audio.PlayerViewHolder
import com.bpzzr.commonlibrary.DynamicPermission
import com.bpzzr.commonlibrary.LogUtil

class TestActivity : AppCompatActivity(), DynamicPermission.OnPermissionListener {

    private lateinit var playerViewHolder: PlayerViewHolder
    private val TAG = "TestActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        findViewById<TextView>(R.id.tv_start).setOnClickListener {
            //AudioPlayer.instance.startPlay(this)
        }

        /* NotificationUtil.createNotificationChannel(
             this,
             "播放控制",
             "为了更好的为您服务，不漏掉重要的信息，请尽量开启此通知！",
             "播放控制"
         )
         NotificationUtil.createNotificationChannel(
             this,
             "普通通知",
             "为了更好的为您服务，不漏掉重要的信息，请尽量开启此通知！",
             "普通通知"
         )
         NotificationUtil.createNotificationChannel(
             this,
             "消息推送",
             "为了更好的为您服务，不漏掉重要的信息，请尽量开启此通知！",
             "消息推送"
         )*/
        //NotificationUtil.createDefault(this, AudioFields.CHANNEL_ID)
        //NotificationUtil.createProgress(this, TAG)

        val ll = findViewById<LinearLayout>(R.id.ll_test_container)

        playerViewHolder = PlayerViewHolder(this)
        playerViewHolder.mIvPlay.setOnClickListener {
            AudioPlayer.instance.startPlay(this)
        }
        playerViewHolder.mSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        AudioPlayer.instance.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

        AudioPlayer.instance.setListener(object : AudioPlayer.Companion.AudioListener {
            override fun onStart() {
                LogUtil.e(TAG, "onStart")
            }

            override fun onPrepared(duration: Int?) {
                LogUtil.e(TAG, "onPrepared")
                if (duration != null) {
                    playerViewHolder.mSeekBar.max = duration
                }
            }

            override fun onError() {
                LogUtil.e(TAG, "onError")
            }

            override fun onProgress(position: Int) {
                LogUtil.e(TAG, "position:$position")
                playerViewHolder.mSeekBar.progress = position
            }
        })

        ll.addView(playerViewHolder.mView)
        //AudioPlayer.instance.startPlay()
        val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        DynamicPermission(this, CAMERA_PERMISSIONS, this)
    }

    override fun onPermissionPass() {
        LogUtil.e(TAG, "onPermissionPass()")
    }

    override fun onPermissionDenied(permission: String?): Boolean {
        LogUtil.e(TAG, "onPermissionDenied($permission)")
        return false
    }
}