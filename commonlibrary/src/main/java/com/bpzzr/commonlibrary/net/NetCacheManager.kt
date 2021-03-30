package com.bpzzr.commonlibrary.net

import android.util.LruCache
import com.bpzzr.commonlibrary.Common
import com.bpzzr.commonlibrary.util.LogUtil
import com.bpzzr.commonlibrary.util.MD5Util
import com.google.gson.Gson
import okio.*
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

/**
 * 网络缓存管理
 * 1、从内存中存取（总内存的八分之一）
 * 2、从磁盘中存取
 */
class NetCacheManager private constructor() {
    private var mLruCache: LruCache<String, String>

    companion object {
        const val TAG = "NetCacheManager"
        const val NET_CACHE_DIR = "baseNetCache/"
        var cacheDir = NET_CACHE_DIR

        val instance: NetCacheManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetCacheManager()
        }
    }

    init {
        val maxMemory = (Runtime.getRuntime().totalMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        //内存缓存
        mLruCache = LruCache(cacheSize)
    }

    fun saveCache(url: String?, data: String) {
        if (url.isNullOrBlank() || data.isBlank()) {
            return
        }
        val cache = getCache(url)
        if (cache == data) {
            return
        }
        //只存变化了的数据
        saveInMem(url, data)
        saveInDisk(url, data)
    }

    fun getCache(url: String?): String {
        if (url.isNullOrBlank()) {
            return ""
        }
        val inMem = getInMem(url)
        if (!inMem.isNullOrBlank()) {
            return inMem
        }
        val inDisk = getInDisk(url)
        if (inDisk.isNotBlank()) {
            return inDisk
        }
        return ""
    }

    /**
     * 缓存至内存
     *
     * @param url  url
     * @param data 对应的数据
     */
    fun saveInMem(url: String?, data: String) {
        if (url.isNullOrBlank() || data.isBlank()) {
            //url或数据为空串，则不缓存
            return
        }
        mLruCache.put(MD5Util.md5(url), data)
    }

    /**
     * 获取内存缓存
     *
     * @param url url
     * @return 返回对应的数据
     */
    fun getInMem(url: String?): String? {
        return mLruCache[MD5Util.md5(url)]
    }

    /**
     * 缓存到磁盘
     * @param url url
     * @param data 数据
     * @param cacheDir 缓存目录
     */
    fun saveInDisk(url: String?, data: String?, cacheDir: String = NET_CACHE_DIR) {
        if (url.isNullOrBlank() || data.isNullOrBlank()) {
            return
        }
        //写入文件
        val dir = File(Common.mContext.cacheDir, cacheDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val md5 = MD5Util.md5(url)
        val file = File(dir, md5!!)
        val sink: Sink = file.sink()
        sink.buffer().write(data.toByteArray(Charset.defaultCharset())).close()
    }

    /**
     * 从磁盘取缓存
     * @param url url
     * @param data 数据
     * @param cacheDir 缓存目录
     */
    fun getInDisk(url: String?, cacheDir: String = NET_CACHE_DIR): String {
        //读取磁盘文件
        val dir = File(Common.mContext.cacheDir, cacheDir)
        if (!dir.exists()) {
            return ""
        }
        val md5 = MD5Util.md5(url)
        val file = File(dir, md5!!)
        if (!file.exists()) {
            return ""
        }
        val data = file.source().buffer().readUtf8()
        LogUtil.e(TAG, "cache data: $data")
        return data
    }
}