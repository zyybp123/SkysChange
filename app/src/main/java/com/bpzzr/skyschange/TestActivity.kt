package com.bpzzr.skyschange

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RemoteViews
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bpzzr.audiolibrary.AudioFields
import com.bpzzr.audiolibrary.audio.AudioPlayer
import com.bpzzr.audiolibrary.audio.AudioService
import com.bpzzr.audiolibrary.audio.PlayerViewHolder
import com.bpzzr.commonlibrary.CommonDialog
import com.bpzzr.commonlibrary.DynamicPermission
import com.bpzzr.commonlibrary.util.LogUtil
import com.bpzzr.commonlibrary.widget.StateLayout
import com.bpzzr.skyschange.databinding.ActivityTestBinding
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestActivity : AppCompatActivity(), DynamicPermission.OnPermissionListener {

    private lateinit var playerViewHolder: PlayerViewHolder
    private val TAG = "TestActivity"
    private val cameraPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_test)
        binding = ActivityTestBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val d = CommonDialog(activity = this)

        /*.buildDefaultTipDialog(
            "测试弹窗",
            "中间描述中间描述中间描述中间描述中间描述中间描述" +
                    "中间描述中间描述中间描述中间描述中间描述中间描述",
            "知道了"
        ).addBottomAction("取消", object : CommonDialog.BottomAction {
            override fun action(tvAction: TextView) {
                tvAction.setTextColor(Color.RED)
            }
        })*/
        binding.tvStart.setOnClickListener {
            //AudioPlayer.instance.startPlay(this)
            /* d.buildEditDialog(
                 title = "测试弹窗",
                 hint = "请输入内容",
                 getter = object : CommonDialog.EditDialog() {
                     override fun textGet(text: String) {
                         Toast.makeText(
                             applicationContext,
                             "输入内容：$text", Toast.LENGTH_LONG
                         ).show()
                     }
                 }
             ).show()*/
            //val supportedConversation: MutableMap<String, Boolean> = HashMap()
            //supportedConversation[Conversation.ConversationType.PRIVATE.getName()] = false
            RongIM.getInstance().startConversationList(
                this@TestActivity,
            )
            //TestFragmentActivity.start(this@TestActivity)
        }

        //AudioService.startAudioService(this)
        //DynamicPermission(this, cameraPermissions, this)
        //val s: StateLayout = findViewById(R.id.state_layout)
        binding.stateLayout.config = StateLayout.Config(
            context = this,
            //failImage = R.drawable.audio_icon_logo_default
        )
        //s.showEmpty(stateString = "暂无相关数据，点击重试")
        binding.stateLayout.showLoading()
        binding.stateLayout.addSuccessView(PlayerViewHolder(this@TestActivity).mView)

        //lifecycleScope

        // 在后台启动一个新的协程并继续// 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
        GlobalScope.launch(Dispatchers.Main) {

            delay(1000L)
            binding.stateLayout.showSuccess()
            delay(1000L)
            binding.stateLayout.showFail()
        }

    }

    fun CommonDialog.extend() {

    }

    fun test() {
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

        createCustom(this)
    }


    private fun createCustom(context: Context) {
        val notificationLayout = RemoteViews(
            context.packageName,
            R.layout.audio_lib_player_view_notification
        )
        val audioControlNotification = NotificationCompat.Builder(
            context, AudioFields.CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_dialog_email)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCustomBigContentView(notificationLayout)
//            .setV
            .build()
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(100, audioControlNotification)
        }
    }

    override fun onPermissionPass() {
        LogUtil.e(TAG, "onPermissionPass()")
    }

    override fun onPermissionDenied(permission: String?): Boolean {
        LogUtil.e(TAG, "onPermissionDenied($permission)")
        return false
    }
}