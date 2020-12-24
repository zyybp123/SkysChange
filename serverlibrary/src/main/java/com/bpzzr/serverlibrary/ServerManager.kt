package com.bpzzr.serverlibrary

import com.bpzzr.commonlibrary.util.LogUtil
import org.java_websocket.WebSocket
import java.util.*

class ServerManager {
    private var serverSocket: ServerSocket? = null
    private val userMap: MutableMap<WebSocket?, String?> = HashMap()
    fun UserLogin(userName: String?, socket: WebSocket?) {
        if (userName != null || socket != null) {
            userMap[socket] = userName
        }
    }

    fun UserLeave(socket: WebSocket?) {
        if (userMap.containsKey(socket)) {
            val userName = userMap[socket]
            LogUtil.e("webSocket", "Leave:$userName")
            userMap.remove(socket)
            SendMessageToAll("$userName...Leave...")
        }
    }

    fun AllUserLeave() {
        val ketSet: Set<WebSocket?> = userMap.keys
        for (socket in ketSet) {
            userMap.remove(socket)
            socket!!.close()
        }
    }

    fun SendMessageToUser(socket: WebSocket?, message: String?) {
        socket?.send(message)
    }

    fun SendMessageToUser(userName: String, message: String?) {
        val ketSet: Set<WebSocket?> = userMap.keys
        for (socket in ketSet) {
            val name = userMap[socket]
            if (name != null) {
                if (name == userName) {
                    socket!!.send(message)
                    break
                }
            }
        }
    }

    fun SendMessageToAll(message: String) {
        LogUtil.e("webSocket", "发送消息-----------$message")
        val ketSet: Set<WebSocket?> = userMap.keys
        for (socket in ketSet) {
            val name = userMap[socket]
            if (name != null) {
                socket!!.send(message)
            }
        }
    }

    fun Start(port: Int): Boolean {
        if (port < 0) {
            LogUtil.e("webSocket", "Port error...")
            return false
        }
        LogUtil.e("webSocket", "Start ServerSocket...")

//        WebSocketImpl.DEBUG = false;
        return try {
            serverSocket = ServerSocket(this, port)
            serverSocket!!.start()
            LogUtil.e("webSocket", "Start ServerSocket Success...${serverSocket!!.address}")
            true
        } catch (e: Exception) {
            LogUtil.e("webSocket", "Start Failed...")
            e.printStackTrace()
            false
        }
    }

    fun Stop(): Boolean {
        return try {
            serverSocket!!.stop()
            LogUtil.e("webSocket", "Stop ServerSocket Success...")
            true
        } catch (e: Exception) {
            LogUtil.e("webSocket", "Stop ServerSocket Failed...")
            e.printStackTrace()
            false
        }
    }
}