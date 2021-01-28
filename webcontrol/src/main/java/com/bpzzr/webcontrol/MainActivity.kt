package com.bpzzr.webcontrol

import android.app.Dialog
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bpzzr.commonlibrary.CommonDialog
import com.bpzzr.commonlibrary.util.LogUtil
import com.bpzzr.commonlibrary.util.NetWorkUtil
import com.bpzzr.webcontrol.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.io.File
import java.lang.Exception
import java.net.InetSocketAddress

class MainActivity : AppCompatActivity() {
    private val mTag = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    private var socketServer: WebSocketServer? = null
    private var socket: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLaunch.setOnClickListener {
            socketServer = launchServer()
        }
        binding.btnEnter.setOnClickListener {
            socket?.send(ENTER_COMMAND)
            binding.tvTime.text = ""
        }
        binding.btnSpace.setOnClickListener {
            socket?.send(SPACE_COMMAND)
        }

        binding.btnTest.setOnClickListener {
            fileProviderShare()
        }
    }

    private fun launchServer(): WebSocketServer {
        val socketServer = object : WebSocketServer(InetSocketAddress(8080)) {
            override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
                LogUtil.e(mTag, "客户端已连接：$conn")
                //只连接一个客户端
                if (socket == null) {
                    socket = conn
                }
            }

            override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
                LogUtil.e(mTag, "服务已关闭")
                socket = null
                binding.btnLaunch.isEnabled = true
            }

            override fun onMessage(conn: WebSocket?, message: String?) {
                LogUtil.e(mTag, "来自页面的消息：$message")
                GlobalScope.launch(Dispatchers.Main) {
                    val data: CountDownData = Gson().fromJson(
                        message, CountDownData::class.java
                    )
                    if (data.command == 0) {
                        binding.tvStatus.text = data.msg
                    }
                    binding.tvTime.text = data.time
                }
            }

            override fun onError(conn: WebSocket?, ex: Exception?) {
                LogUtil.e(mTag, "服务异常：$ex")
                socket = null
                binding.btnLaunch.isEnabled = true
            }

            override fun onStart() {
                LogUtil.e(mTag, "服务启动成功")
                GlobalScope.launch(Dispatchers.Main) {
                    binding.btnLaunch.isEnabled = false
                    binding.btnLaunch.text = "服务已启动，请打开网页"
                }
            }
        }
        socketServer.start()
        return socketServer
    }

    override fun onDestroy() {
        socketServer?.stop()
        socket = null
        socketServer = null
        super.onDestroy()
    }

    companion object {
        private const val SPACE_COMMAND: String = "space_command"
        private const val ENTER_COMMAND: String = "enter_command"
    }

    override fun onBackPressed() {
        if (socket == null) {
            super.onBackPressed()
        } else {
            val d = CommonDialog(activity = this)
                .buildDefaultTipDialog(
                    "提示",
                    "服务运行中，确定要退出吗",
                    "确定",
                    object : CommonDialog.DefaultTipDialog {
                        override fun elementGet(
                            tvTitle: TextView,
                            tvSure: TextView,
                            dialog: Dialog?
                        ) {
                            tvSure.setOnClickListener {
                                finish()
                            }
                        }
                    }
                )
                .addBottomAction("取消", object : CommonDialog.BottomAction {
                    override fun action(tvAction: TextView, dialog: CommonDialog?) {
                        tvAction.setOnClickListener {
                            dialog?.dismiss()
                        }
                    }
                })
            d.show()
        }
    }


    private fun fileProviderShare(): String? {
        val imagePath = this.getExternalFilesDir(null)
        val file = File(imagePath, "Test/aa.jpg")

        if (!file.parentFile?.exists()!!) {
            file.parentFile?.mkdirs()
        }

        //更具获取Url
        val contentUri: Uri =
            FileProvider.getUriForFile(
                this,
                "$packageName.FileProvider", file
            )
        val fileOutputStream = contentResolver.openOutputStream(contentUri) ?: return null
        val inputStream = this.assets.open("friends.jpg")
        val byteArray = ByteArray(1024)
        try {
            fileOutputStream.use { outputStream ->
                inputStream.use { inputStream ->
                    while (true) {
                        val readLen = inputStream.read(byteArray)
                        if (readLen == -1) {
                            break
                        }
                        outputStream.write(byteArray, 0, readLen)
                    }
                }
            }
        } catch (e: Throwable) {
            LogUtil.e(mTag, "fileProviderShare e:$e")
        }
        LogUtil.e(mTag, "fileProviderShare:$contentUri")
        return contentUri.toString()
    }
}