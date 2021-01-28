package com.bpzzr.skyschange

import android.Manifest
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RemoteViews
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bpzzr.audiolibrary.AudioFields
import com.bpzzr.audiolibrary.audio.AudioPlayer
import com.bpzzr.audiolibrary.audio.PlayerViewHolder
import com.bpzzr.commonlibrary.CommonDialog
import com.bpzzr.commonlibrary.DynamicPermission
import com.bpzzr.commonlibrary.file.BaseFileEntity
import com.bpzzr.commonlibrary.file.FileScanner
import com.bpzzr.commonlibrary.file.FileType
import com.bpzzr.commonlibrary.file.ScanType
import com.bpzzr.commonlibrary.util.DimensionUtil
import com.bpzzr.commonlibrary.util.LogUtil
import com.bpzzr.commonlibrary.widget.StateLayout
import com.bpzzr.skyschange.databinding.ActivityTestBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.java_websocket.WebSocket
import java.io.File
import java.util.*


class TestActivity : AppCompatActivity(), DynamicPermission.OnPermissionListener {

    private lateinit var playerViewHolder: PlayerViewHolder
    private val TAG = "TestActivity"
    private val cameraPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private lateinit var binding: ActivityTestBinding
    private lateinit var socket: WebSocket

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.stateLayout.config = StateLayout.Config(
            context = this,
        )
        //s.showEmpty(stateString = "暂无相关数据，点击重试")
        binding.stateLayout.showLoading()

        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val mDataList = ArrayList<BaseFileEntity>()
        val simpleTextAdapter = SimpleTextAdapter(mDataList)
        recyclerView.adapter = simpleTextAdapter
        binding.stateLayout.addSuccessView(recyclerView)

        val file = File(getExternalFilesDir(null), "test")
        //
        val pf = file.parentFile
        LogUtil.e(TAG, "pf: $pf")
        val ppf = pf?.parentFile
        LogUtil.e(TAG, "ppf: $ppf")
        val tf = File(pf, "com.tencent.moblieqq")


        /*LogUtil.e(TAG, "p: ${Environment.getExternalStorageDirectory()}")
        val file1 = File("/sdcard/Android/data/xysx.com.tzq", "Test")
        if (!file1.exists()) {
            LogUtil.e(TAG, "mk: ${file1.mkdirs()}")
        }*/
        //val str = file1.source().buffer().readUtf8()
        //LogUtil.e(TAG, "test str: $str")

//        val requestFileIntent = Intent(Intent.ACTION_PICK).apply {
//            type = "*/*"
//        }
//
//        val uriForFile = FileProvider.getUriForFile(
//            this,
//            "xysx.com.tzq",
//            file1
//        )
//        LogUtil.e(TAG, "test str: $uriForFile")


        //startActivityForResult(requestFileIntent, 0)
        /*val launcher = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            run {
                LogUtil.e(TAG, "$uri")
            }
        }*/

        //launcher.launch(requestFileIntent)
        //startActivityForResult(requestFileIntent, 0)

        //externalMediaDirs
        //LogUtil.e(TAG, "media: ${Arrays.toString(externalMediaDirs)}")
        LogUtil.e(TAG, "media: ${getDir(packageName, MODE_PRIVATE)}}")
        //LogUtil.e(TAG, Arrays.toString(getExternalFilesDirs("com.tencent.edu")))
        /*LogUtil.e(TAG, Arrays.toString(getExternalFilesDirs(null)))
       LogUtil.e(TAG, Environment.getExternalStorageDirectory().absolutePath)
       LogUtil.e(
           TAG, Environment.getExternalStoragePublicDirectory(
               "/Android/data/com.tencent.edu"
           ).absolutePath
       )*/

        FileScanner().startScanFile(this,
            ScanType.SCAN_ALL, FileType.ALL_LIST, object : FileScanner.ResultListener {
                override fun onResult(files: ArrayList<BaseFileEntity>) {
                    LogUtil.e(files.toString())
                    mDataList.clear()
                    mDataList.addAll(files)
                    simpleTextAdapter.notifyDataSetChanged()
                    binding.stateLayout.showSuccess()
                }

                override fun onError(e: Exception) {
                    LogUtil.e("error：$e")
                    binding.stateLayout.showFail()
                }
            })

        /*FileScanner().insertMediaFile(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            this,null,"123",
            "123","123","Tencent","Tencent"
        )*/

        binding.tvStart.setOnClickListener {
            /* ServerCore().launchServer(object :ServerCore.Listener{
                 override fun onConnected(webSocket: WebSocket) {
                     socket = webSocket
                 }
             })*/
            //ServerManager().Start(8080)
        }
        val d = CommonDialog(activity = this)
        //binding.stateLayout.visibility = View.INVISIBLE
        val px = DimensionUtil.getPxSize(this, R.dimen.dp_100)
        binding.tvTest.setOnClickListener {

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtil.e(TAG, "data:  $data")
    }

    fun CommonDialog.extend() {

    }

    fun test() {

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
        /*RongIM.getInstance().startConversationList(
            this@TestActivity,
        )*/
        //TestFragmentActivity.start(this@TestActivity)
        val ll = findViewById<LinearLayout>(R.id.ll_test_container)

        playerViewHolder = PlayerViewHolder(this)
        playerViewHolder.mIvPlay.setOnClickListener {
            //AudioPlayer.instance.startPlay(this)
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

            override fun onError(mediaPlayer: MediaPlayer?, what: Int, extra: Int) {
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