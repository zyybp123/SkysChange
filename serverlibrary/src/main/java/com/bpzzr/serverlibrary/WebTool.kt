package com.bpzzr.serverlibrary

import com.bpzzr.commonlibrary.util.LogUtil
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class WebTool private constructor() {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .writeTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    fun setRequest() {}

    //sample
    fun webSocketRequest(url: String?) {
        val request: Request? = url?.let { Request.Builder().url(it).build() }
        val socket = request?.let {
            client.newWebSocket(it, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    //连接成功
                    LogUtil.e(TAG, "连接成功")
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    //收到服务器发来的消息
                    LogUtil.e(TAG, "text: $text")
                    //可以向服务器发送移动端的消息
                    webSocket.send("aaa")
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    super.onMessage(webSocket, bytes)
                    //收到服务器发来的消息
                    LogUtil.e(TAG, "收到服务器发来的消息")
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosing(webSocket, code, reason)
                    //连接关闭
                    LogUtil.e(TAG, "连接关闭")
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                    //连接关闭
                    LogUtil.e(TAG, "连接关闭, closed")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    //连接失败处理
                    LogUtil.e(TAG, "连接失败: $webSocket, t: $t, response: $response")
                }
            })
        }
    }

    companion object {
        private const val TAG = "WebTool"

        @Volatile
        private var mInstance: WebTool? = null
        val instance: WebTool?
            get() {
                if (mInstance == null) {
                    synchronized(WebTool::class.java) {
                        if (mInstance == null) {
                            mInstance = WebTool()
                        }
                    }
                }
                return mInstance
            }
    }

}