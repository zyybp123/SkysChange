package com.bpzzr.commonlibrary.net.interceptor

import com.bpzzr.commonlibrary.net.NetCacheManager
import com.bpzzr.commonlibrary.util.LogUtil
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import java.nio.charset.Charset
import java.util.*

/**
 * 处理缓存拦截器
 * 应对post请求的缓存问题
 * 上传、下载请勿设置缓存头
 */
class CacheInterceptor : Interceptor {
    companion object {
        const val TAG = "CacheInterceptor"
        const val CACHE_CONTROL = "cmCacheControl"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder: Request.Builder = request.newBuilder()
        val cacheHeader = request.headers(CACHE_CONTROL)

        //缓存控制
        var needCache = false
        if (cacheHeader.isNotEmpty()) {
            builder.removeHeader(CACHE_CONTROL)
            needCache = true
        }
        if (needCache) {
            val requestBody = request.body
            LogUtil.e(TAG, "url: " + request.url.toString());
            if ("GET" == request.method) {
                //get请求,框架自主处理
                return chain.proceed(builder.build())
            }
            if ("POST" == request.method) {
                val mediaType = requestBody?.contentType()
                if ("multipart" == mediaType?.type?.toLowerCase(Locale.ROOT)) {
                    //二进制流不处理
                    return chain.proceed(builder.build())
                }
            }
            //获取响应体(注意，responseBody只能使用一次，缓存完毕需要重构响应)
            val body = chain.proceed(builder.build()).body
            val mediaType = body?.contentType()
            val bytes = body?.bytes()
            //处理缓存
            NetCacheManager.instance.saveCache(
                request.url.toString(),
                String(bytes!!, Charset.defaultCharset())
            )
            //重构响应体返回
            return Response.Builder()
                .body(bytes.toResponseBody(mediaType))
                .build()
        }
        return chain.proceed(builder.build())
    }
}