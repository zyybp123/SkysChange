package com.bpzzr.commonlibrary.net

import android.content.Context
import android.util.Log
import com.bpzzr.commonlibrary.BuildConfig
import com.bpzzr.commonlibrary.Common
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Administrator on 2017/9/21.
 * Glide全局配置
 */
@GlideModule
class GlideManager : AppGlideModule() {
    /**
     * 取1/8最大内存作为最大缓存
     */
    private val memorySize = Runtime.getRuntime().maxMemory().toInt() / 8
    private val requestOptions = RequestOptions()
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // 自定义内存和图片池大小
        builder.setMemoryCache(LruResourceCache(memorySize.toLong()))
        builder.setBitmapPool(LruBitmapPool(memorySize.toLong()))
        //设置磁盘缓存大小500M
        val diskSize = 1024 * 1024 * 500
        //磁盘缓存采用lruCache策略，指定数据的缓存地址(SD卡cache目录下的appImgCache)
        builder.setDiskCache(
            ExternalPreferredCacheDiskCacheFactory(
                context, Common.DISK_CACHE_NAME, diskSize.toLong()
            )
        )
        //设置默认的缓存图片格式
        builder.setDefaultRequestOptions(
            requestOptions.format(DecodeFormat.PREFER_ARGB_8888)
        )
        //设置日志级别,为调试模式
        builder.setLogLevel(Log.DEBUG)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        //组件注册
        //OkHttpClient client = RetrofitTool.getInstance().getOkClient();
        //注册ok http 进度监听
        //registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
        //注册模型解析器
        //registry.prepend(ParcelFileDescriptor.class, InputStream.class, new MyModelFactory());
    }
}