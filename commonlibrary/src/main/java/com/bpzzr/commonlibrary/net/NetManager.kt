package com.bpzzr.commonlibrary.net

import androidx.collection.SimpleArrayMap
import com.bpzzr.commonlibrary.Common
import com.bpzzr.commonlibrary.net.factory.FastJsonConverterFactory
import com.bpzzr.commonlibrary.net.interceptor.BaseInterceptor
import com.bpzzr.commonlibrary.net.interceptor.CacheInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


class NetManager private constructor() {

    companion object {
        const val READ_TIME_OUT = 30
        const val CONNECT_TIME_OUT = 3
        const val WRITE_TIME_OUT = 30
        const val CACHE_DIR = "okCache/"
        const val MAX_SIZE = 100 * 1024 * 1024
        const val BASE_URL = "https://www"

        val instance: NetManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetManager()
        }

        //维护需要发起的请求的主机映射
        var mUrlMap: SimpleArrayMap<String, String> = SimpleArrayMap()

        //公共的静态请求头
        var mHeaderMap: SimpleArrayMap<String, String> = SimpleArrayMap()
    }

    init {
        val cache = Cache(File(Common.DISK_CACHE_NAME, CACHE_DIR), MAX_SIZE.toLong())
        val okClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.MINUTES)
            .connectTimeout(CONNECT_TIME_OUT.toLong(), TimeUnit.MINUTES)
            .writeTimeout(WRITE_TIME_OUT.toLong(), TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .addInterceptor(BaseInterceptor())
            .addInterceptor(CacheInterceptor())
            .cache(cache)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            //.addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(FastJsonConverterFactory.create())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


}