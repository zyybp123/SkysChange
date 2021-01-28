package com.bpzzr.skyschange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bpzzr.commonlibrary.util.LogUtil
import okio.buffer
import okio.source
import java.io.File
import java.util.*

class StoreActivity : AppCompatActivity() {
    companion object {
        val TAG = "StoreActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_store)
        LogUtil.e(TAG, "getDir: ${getDir("test", MODE_PRIVATE)}")
        LogUtil.e(TAG, "getExternalFilesDirs: ${Arrays.toString(getExternalFilesDirs("test"))}")
        LogUtil.e(TAG, "getExternalFilesDir: ${getExternalFilesDir("test")}")
        LogUtil.e(TAG, "externalCacheDir: ${externalCacheDir?.absolutePath}")
        LogUtil.e(TAG, "externalCacheDirs: ${Arrays.toString(externalCacheDirs)}")
        LogUtil.e(TAG, "externalMediaDirs: ${Arrays.toString(externalMediaDirs)}")
        //get
        val file = getExternalFilesDirs("test")[1]
        //
        val pf = file.parentFile
        LogUtil.e(TAG, "pf: $pf")
        val ppf = pf?.parentFile?.parentFile
        LogUtil.e(TAG, "ppf: $ppf")
        val tf = File(ppf, "xysx.com.tzq/Test/test.txt")
        LogUtil.e(TAG,"tf: $tf")
        val str = tf.source().buffer().readUtf8()
        LogUtil.e(TAG, "test str: $str")
    }
}