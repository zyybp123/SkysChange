package com.bpzzr.serverlibrary

import com.bpzzr.commonlibrary.util.LogUtil
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import org.json.JSONException
import org.json.JSONObject
import java.net.InetSocketAddress

class ServerSocket(private val serverManager: ServerManager, port: Int) : WebSocketServer(
    InetSocketAddress(port)
) {
    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        LogUtil.e("webSocket", "Some one Connected..............")
        serverManager.UserLogin("server", conn)
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        LogUtil.e("webSocket", "socket  closing....................")
        serverManager.UserLeave(conn)
    }

    override fun onMessage(conn: WebSocket, message: String) {
        try {
            val jo = JSONObject(message)
            val command_code = jo.getString("command_code")
            //            MsgBean msgBean = new MsgBean();
//            msgBean.setCode(command_code);
//            EventBus.getDefault().post(msgBean);
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onError(conn: WebSocket, ex: Exception) {
        LogUtil.e("webSocket", "Socket Exception:$ex")
        //        MsgBean msgBean = new MsgBean();
//        msgBean.setCode(20000+"");
//        EventBus.getDefault().post(msgBean);
    }

    override fun onStart() {}
}