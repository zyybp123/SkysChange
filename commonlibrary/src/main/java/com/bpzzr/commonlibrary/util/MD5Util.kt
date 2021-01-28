package com.bpzzr.commonlibrary.util

import androidx.collection.SimpleArrayMap
import okhttp3.internal.and
import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.security.MessageDigest

/**
 * MD5相关工具类
 */
class MD5Util private constructor() {

    companion object {
        private const val MD5 = "MD5"
        private const val TAG = "MD5Util"
        private const val FR_RADIX = 16
        private const val FR_SIGN_NUM = 1

        /**
         * 将字符串用md5加密
         *
         * @param str 字符串
         * @return 如果字符串为空串，则原样返回
         */
        fun md5(str: String?): String? {
            if (str.isNullOrBlank()) {
                return str
            }
            var hexString: String? = null
            return try {
                val sb = StringBuilder()
                val md = MessageDigest.getInstance(MD5)
                val digest = md.digest(str.toByteArray())
                for (b in digest) {
                    val temp: Int = b and 0xFF
                    hexString = Integer.toHexString(temp)
                    if (hexString.length < 2) {
                        sb.append("0")
                    }
                    sb.append(hexString)
                }
                sb.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtil.e(TAG, "get md5 exception: $e")
                str
            }
        }

        /**
         * 获取单个文件的MD5值！
         *
         * @param filePath 文件的路径
         * @return 空串或不是文件会返回null
         */
        fun getFileMD5(filePath: String?): String? {
            if (filePath.isNullOrBlank()) {
                //为空串，直接返回null
                return null
            }
            val file = File(filePath)
            if (!file.isFile) {
                //不是文件，直接返回null
                return null
            }
            var digest: MessageDigest? = null
            var `in`: FileInputStream? = null
            val buffer = ByteArray(1024)
            var len: Int
            try {
                digest = MessageDigest.getInstance(MD5)
                `in` = FileInputStream(file)
                while (`in`.read(buffer, 0, 1024).also { len = it } != -1) {
                    digest.update(buffer, 0, len)
                }
                `in`.close()
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtil.e(TAG, "get md5 exception: $e")
                return null
            }
            val bigInt = BigInteger(FR_SIGN_NUM, digest.digest())
            return bigInt.toString(FR_RADIX)
        }

        /**
         * 获取文件夹中文件的MD5值
         *
         * @param filePath  文件夹的绝对路径
         * @param listChild 是否递归子目录中的文件 true为是
         * @param map       存MD5的map
         * @return 返回对应文件的路径和对应的MD5值的map
         */
        fun getDirMD5(
            filePath: String?,
            map: SimpleArrayMap<String, String>, listChild: Boolean
        ): SimpleArrayMap<String, String>? {
            if (filePath.isNullOrBlank()) {
                //为空串直接返回null
                return null
            }
            val file = File(filePath)
            if (!file.isDirectory) {
                //不是文件夹，返回null
                return null
            }
            var md5: String?
            val files = file.listFiles() ?: return map
            for (f in files) {
                if (f.isDirectory && listChild) {
                    getDirMD5(f.absolutePath, map, true)
                } else {
                    md5 = getFileMD5(f.absolutePath)
                    if (md5 != null) {
                        map.put(f.path, md5)
                    }
                }
            }
            return map
        }
    }
}