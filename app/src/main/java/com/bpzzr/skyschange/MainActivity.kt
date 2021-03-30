package com.bpzzr.skyschange

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bpzzr.commonlibrary.util.LogUtil
import com.bpzzr.commonlibrary.util.NetWorkUtil
import com.bpzzr.commonlibrary.widget.FloatWidget.Companion.create
import com.bpzzr.skyschange.home.HomeActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_next).setOnClickListener { goNext() }
        //ManagerLib.connectRong(
        //    "alKvTb24v6CzwdQ1Mlq4n5BZn+T8TlRxXOa/j1rM422AMWMFQRoeKw==@pjv5.cn.rongnav.com;pjv5.cn.rongcfg.com")
        //AudioPlayer.instance.startPlay()

        LogUtil.e("ip: ${NetWorkUtil.getServerAddressByWifi(this)}")

        //homeBinding.rcViewPager.setLayoutDirection();
        val textView = TextView(this)
        textView.text = "这是个浮窗"
        textView.setTextColor(Color.RED)
        /*textView.setOnClickListener {
            Toast.makeText(this, "点击", Toast.LENGTH_SHORT).show()
        }*/
        create(this, textView)
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