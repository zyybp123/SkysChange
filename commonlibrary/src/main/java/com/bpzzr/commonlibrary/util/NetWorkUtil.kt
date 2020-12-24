package com.bpzzr.commonlibrary.util

import android.content.Context
import android.content.Intent
import android.net.*
import android.net.wifi.WifiManager
import android.provider.Settings


public class NetWorkUtil {


    companion object {
        private const val mTag = "NetWorkUtil"

        fun openWirelessSettings(context: Context) {
            context.startActivity(
                Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
        }

        /**
         * 注册网络监听器
         */
        fun registerNetworkStatusChangedListener(
            context: Context,
            callback: ConnectivityManager.NetworkCallback
        ) {
            val systemService = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
            if (systemService is ConnectivityManager) {
                systemService.registerNetworkCallback(
                    NetworkRequest.Builder().build(),
                    callback
                )
            }
        }

        fun getServerAddressByWifi(context: Context): String {
            val wm =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val ipStr = wm.dhcpInfo.ipAddress.toString()
            LogUtil.e(mTag, "ipStr: $ipStr")
            return intToIp(wm.dhcpInfo.ipAddress)
        }


        fun intToIp(ipInt: Int): String {
            val sb = StringBuilder()
            sb.append(ipInt and 0xFF).append(".")
            sb.append(ipInt shr 8 and 0xFF).append(".")
            sb.append(ipInt shr 16 and 0xFF).append(".")
            sb.append(ipInt shr 24 and 0xFF)
            return sb.toString()
        }

        /**
         * 获取连接WiFi时的ip地址
         */
        fun getWifiIp(context: Context): String {
            val wifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
            val wifiInfo = wifiManager!!.connectionInfo
            val ipAddress = wifiInfo.ipAddress
            val intToIp = intToIp(ipAddress)
            LogUtil.e(mTag, "wifi_ip地址为:$intToIp")
            return intToIp
        }


    }

     open class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {
        private val mTag = "NetworkCallbackImpl"
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            LogUtil.d(mTag, "network is onAvailable: $network")
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            LogUtil.d(mTag, "network is onLosing: ($network, $maxMsToLive)")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            LogUtil.d(mTag, "network is onLost: $network")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            LogUtil.d(mTag, "network is onUnavailable")
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                       LogUtil.d(mTag, "onCapabilitiesChanged: 网络类型为wifi");
                    }
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        LogUtil.d(mTag, "onCapabilitiesChanged: 蜂窝网络");
                    }
                    else -> {
                        LogUtil.d(mTag, "onCapabilitiesChanged: 其他网络");
                    }
                }
                LogUtil.d(mTag, "onCapabilitiesChanged: $network, $networkCapabilities");
            }
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
            LogUtil.d(mTag, "onLinkPropertiesChanged: $network, $linkProperties");
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
            LogUtil.d(mTag, "onBlockedStatusChanged: $network, $blocked");
        }

    }

}