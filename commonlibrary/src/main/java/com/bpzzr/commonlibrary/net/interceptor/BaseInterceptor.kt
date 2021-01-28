package com.bpzzr.commonlibrary.net.interceptor

import android.text.TextUtils
import android.webkit.URLUtil
import com.bpzzr.commonlibrary.net.NetManager
import com.bpzzr.commonlibrary.util.LogUtil
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 基础拦截器
 * 处理base_url变换的问题
 */
class BaseInterceptor : Interceptor {
    companion object {
        const val TAG = "BaseInterceptor"
        const val URL_FLAG = "cmUrlFlag"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder: Request.Builder = request.newBuilder()
        val urlValues = request.headers(URL_FLAG)

        //注入静态请求头
        LogUtil.d(TAG, "headerMap: ${NetManager.mHeaderMap}")
        for (i in 0 until NetManager.mHeaderMap.size()) {
            val name: String = NetManager.mHeaderMap.keyAt(i)
            val value: String = NetManager.mHeaderMap.valueAt(i)
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
                builder.addHeader(name, value)
            }
        }
        var newUrl: String? = null
        builder.removeHeader(URL_FLAG)
        val headerValue = urlValues[0]
        if (!TextUtils.isEmpty(headerValue)) {
            newUrl = NetManager.mUrlMap.get(headerValue)
        }

        LogUtil.d(TAG, "newUrl:$newUrl")
        if (newUrl == null || !URLUtil.isNetworkUrl(newUrl)) {
            return chain.proceed(builder.build())
        }

        val newBaseUrl = newUrl.toHttpUrlOrNull()
        LogUtil.d(TAG, "newBaseUrl: $newBaseUrl")
        if (newBaseUrl == null) {
            return chain.proceed(builder.build())
        }
        val oldHttpUrl = request.url
        //重构请求
        val newFullUrl = oldHttpUrl
            .newBuilder()
            .scheme(newBaseUrl.scheme)
            .host(newBaseUrl.host)
            .port(newBaseUrl.port)
            .build()
        LogUtil.d(TAG, "new full url : $newFullUrl")
        val newRequest = builder.url(newFullUrl).build()
        return chain.proceed(newRequest)
    }
}