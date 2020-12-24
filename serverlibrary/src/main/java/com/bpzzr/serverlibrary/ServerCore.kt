package com.bpzzr.serverlibrary

import com.bpzzr.commonlibrary.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.buffer
import okio.source
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.nio.ByteBuffer


class ServerCore {
    private val mTag = "ServerCore"

    interface Listener {
        fun onConnected(webSocket: WebSocket)
    }

    fun launchServer(param: Listener) {
        GlobalScope.launch(Dispatchers.IO) {
            // 192.168.1.101为安卓服务端，需要连接wifi后 高级选项ip设置为静态,输入自定义地址
            // 方便客户端 找 服务端,不需要用getHostAddress等，可能连接不上
            // 9090为端口
            val myHost = InetSocketAddress(8080)
            val myWebSocketServer = object : WebSocketServer(myHost) {

                override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
                    LogUtil.e(
                        mTag, "一个客户端连接成功, onOpen()：" + conn!!.remoteSocketAddress
                    )
                    param.onConnected(conn)
                }

                override fun onClose(
                    conn: WebSocket?,
                    code: Int,
                    reason: String?,
                    remote: Boolean
                ) {
                    LogUtil.e(mTag, "服务器关闭, onClose: ")
                }

                override fun onMessage(conn: WebSocket?, message: String?) {

                    // 这里在网页测试端发过来的是文本数据 测试网页 http://www.websocket-test.com/
                    // 需要保证android 和 加载网页的设备(我这边是电脑) 在同一个网段内，连同一个wifi即可
                    //Log.d("websocket", "onMessage()网页端来的消息->"+message);
                    LogUtil.e(mTag, "网页端来的消息(string): $message")
                }

                override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
                    LogUtil.e(mTag, "网页端来的消息(byte): $message")
                }

                override fun onError(conn: WebSocket?, ex: java.lang.Exception?) {
                    // 异常  经常调试的话会有缓存，导致下一次调试启动时，端口未关闭,多等待一会儿
                    // 可以在这里回调处理，关闭连接，开个线程重新连接调用startMyWebsocketServer()
                    LogUtil.e(mTag, "出现异常：$ex");

                }

                override fun onStart() {
                    LogUtil.e(mTag, "onStart()，WebSocket服务端启动成功${this.address}");
                }
            }
            myWebSocketServer.start()
        }
    }

    fun startSocketServer(port: Int = 8080) {
        //HttpServerRequestCallback
        GlobalScope.launch(Dispatchers.IO) {
            try {
                //端口绑定
                val serverSocket = ServerSocket(port)
                //保持链接
                while (true) {
                    val socket: Socket = serverSocket.accept()
                    LogUtil.e(mTag, "socket: $socket")
                    getMessage(socket)
                    //sendMessage(socket, "{\"123\":\"张三\"}")
                    socket.getOutputStream().write("啦啦啦啦啦啦".toByteArray())
                    socket.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 发送消息
     */
    fun sendMessage(socket: Socket, message: String) {
        //GlobalScope.launch(Dispatchers.IO){
        val outputStream = socket.getOutputStream()
        outputStream.write(
            ("HTTP/1.1 200 OK\r\n" +  //响应头第一行
                    "Content-Type: application/json; charset=utf-8\r\n" +  //简单放一个头部信息
                    "" +
                    "\r\n" +  //这个空行是来分隔请求头与请求体的
                    "$message\r\n").toByteArray()
        )
        //outputStream.close()
        //socket.close()
        //socket.sendUrgentData(12)
        //}
    }

    fun getMessage(socket: Socket) {
        //val inputStream = socket.getInputStream()
        //val msg = String(inputStream.readBytes())
        //LogUtil.e(mTag, msg)
        try {
            val msg = socket.getInputStream().source().buffer().readUtf8()
            LogUtil.e(mTag, msg)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dealMessage() {

    }

}