package com.bpzzr.skyschange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bpzzr.audiolibrary.audio.AudioPlayer
import com.bpzzr.commonlibrary.util.LogUtil
import com.bpzzr.commonlibrary.util.NetWorkUtil
import com.bpzzr.managerlib.ManagerLib
import com.bpzzr.skyschange.home.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.InetAddress
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_next).setOnClickListener { goNext() }
        //ManagerLib.connectRong(
        //    "alKvTb24v6CzwdQ1Mlq4n5BZn+T8TlRxXOa/j1rM422AMWMFQRoeKw==@pjv5.cn.rongnav.com;pjv5.cn.rongcfg.com")
        //AudioPlayer.instance.startPlay()

        LogUtil.e("ip: ${NetWorkUtil.getServerAddressByWifi(this)}")

       /* GlobalScope.launch(Dispatchers.IO) {
            try {
                val localHost = InetAddress.getByAddress("aa".toByteArray())
                LogUtil.e("ip: ${localHost.hostAddress}")
            }catch (e:Exception){
                LogUtil.e("ip error: $e")
            }

        }*/
            //LogUtil.e("ip: ${InetAddress.getLocalHost().hostAddress}")
    }

    private fun goNext() {
        //startActivity(Intent(this, StoreActivity::class.java))
        startActivity(Intent(this, HomeActivity::class.java))
        //finish()
    }
}