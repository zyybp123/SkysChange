package com.bpzzr.commonlibrary.file

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.bpzzr.commonlibrary.DynamicPermission
import com.bpzzr.commonlibrary.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*


class FileScanner {
    private val mTag = "FileScanner"

    fun startScanFile(
        activity: AppCompatActivity,
        scanType: Int,
        selectionArg: Array<String>?,
        listener: ResultListener? = null
    ) {
        //先检查权限
        DynamicPermission(activity, arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), object : DynamicPermission.OnPermissionListener {
            override fun onPermissionPass() {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val scanMediaFile = scanMediaFile(activity, scanType, selectionArg)
                        GlobalScope.launch(Dispatchers.Main) {
                            listener?.onResult(scanMediaFile)
                        }
                    } catch (e: Exception) {
                        GlobalScope.launch(Dispatchers.Main) {
                            listener?.onError(e)
                        }
                    }
                }
            }

            override fun onPermissionDenied(permission: String?): Boolean {
                /*Toast.makeText(
                    activity.applicationContext,
                    "您拒绝了", Toast.LENGTH_SHORT
                ).show()*/
                return false
            }
        })

    }

    interface ResultListener {
        fun onResult(files: ArrayList<BaseFileEntity>)
        fun onError(e: Exception)
    }

    /**
     * 查询媒体文件
     *
     * @param context      上下文
     * @param scanType     查询类型，在Android10以下，图片，视频，音频要分开查询
     * 默认图片[ScanType.SCAN_IMAGES]
     * @param selectionArg (查询条件,mime_type [FileType.ALL_LIST])
     */
    fun scanMediaFile(
        context: Context,
        scanType: Int,
        selectionArg: Array<String>?
    ): ArrayList<BaseFileEntity> {
        //要查询的列
        val path =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                MediaStore.Files.FileColumns.RELATIVE_PATH
            else
                MediaStore.Files.FileColumns.DATA
        val columns = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.SIZE, path
        )
        //uri设置
        //Uri uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        var uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        if (scanType == ScanType.SCAN_AUDIOS) {
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        if (scanType == ScanType.SCAN_VIDEOS) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        }
        LogUtil.e(mTag, "uri: $uri")
        //查询条件的拼接
        //SELECT * FROM Persons WHERE (mime_type) IN ('args[0]','arg[1]')
        //按时间排序
        val order = MediaStore.Images.Media.DATE_MODIFIED + " DESC"
        var selections = StringBuilder()
        if (selectionArg != null) {
            for (aSelectionArg in selectionArg) {
                if (!TextUtils.isEmpty(aSelectionArg)) {
                    selections.append("\'").append(aSelectionArg).append("\'").append(",")
                }
            }
            if (selections.length > 1 && selections.toString().endsWith(",")) {
                selections = StringBuilder(selections.substring(0, selections.length - 1))
            }
        }
        val selection = MediaStore.Files.FileColumns.MIME_TYPE + ") IN (" + selections.toString()
        //游标
        val fileList: ArrayList<BaseFileEntity> = ArrayList<BaseFileEntity>()
        val resolver = context.contentResolver
            ?: throw NullPointerException("resolver is null ! ")
        val c = resolver.query(uri, columns, selection, null, order)
        while (c?.moveToNext()!!) {
            val fileId = c.getString(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
            val title = c.getString(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE))
            val type = c.getString(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
            val displayName =
                c.getString(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
            if (TextUtils.isEmpty(displayName)) {
                continue
            }
            val localPath = c.getString(c.getColumnIndexOrThrow(path))
            val dateModify =
                c.getLong(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED))
            val size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
            LogUtil.e(mTag, "displayName: $displayName, title:$title, type:$type")
            val file = BaseFileEntity(
                fileId = fileId,
                name = title,
                nameSuffix = displayName,
                type = type,
                dateModify = dateModify,
                size = size
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                file.relativePath = localPath
            } else {
                file.data = localPath
            }
            fileList.add(file)
        }
        c.close()
        return fileList
    }


    fun loadPhotoFiles(context: Context): List<Uri> {
        LogUtil.e(mTag, "loadPhotoFiles")
        val photoUris: MutableList<Uri> = ArrayList<Uri>()
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(
                MediaStore.Images.Media._ID
            ), null, null, null
        )
        if (cursor != null) {
            LogUtil.e(mTag, "cursor size:" + cursor.count)
        }
        while (cursor?.moveToNext() == true) {
            val id: Int = cursor.getInt(
                cursor.getColumnIndex(MediaStore.Images.Media._ID)
            )
            val photoUri: Uri =
                Uri.parse(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()
                            + File.separator + id
                )
            LogUtil.e(mTag, "photoUri:$photoUri")
            photoUris.add(photoUri)
        }
        cursor?.close()
        return photoUris
    }

    /**
     * 保存多媒体文件到公共集合目录
     * @param uri：多媒体数据库的Uri
     * @param context
     * @param mimeType：需要保存文件的mimeType
     * @param displayName：显示的文件名字
     * @param description：文件描述信息
     * @param saveFileName：需要保存的文件名字
     * @param saveSecondaryDir：保存的二级目录
     * @param savePrimaryDir：保存的一级目录
     * @return 返回插入数据对应的uri
     */
    fun insertMediaFile(
        uri: Uri?,
        context: Context,
        mimeType: String?,
        displayName: String?,
        description: String?,
        saveFileName: String?,
        saveSecondaryDir: String?,
        savePrimaryDir: String?
    ): String? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
        values.put(MediaStore.Images.Media.DESCRIPTION, description)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        values.put(MediaStore.Images.Media.RELATIVE_PATH, savePrimaryDir)
        //values.put(MediaStore.Images.Media.SECONDARY_DIRECTORY, saveSecondaryDir)
        var url: Uri? = null
        var stringUrl: String? = null /* value to be returned */
        val cr = context.contentResolver
        try {
            url = cr.insert(uri!!, values)
            if (url == null) {
                return null
            }
            val buffer = ByteArray(1024)
            val parcelFileDescriptor = cr.openFileDescriptor(url, "w")
            val fileOutputStream = FileOutputStream(parcelFileDescriptor!!.fileDescriptor)
            val inputStream: InputStream = context.resources.assets.open(saveFileName!!)
            while (true) {
                val numRead: Int = inputStream.read(buffer)
                if (numRead == -1) {
                    break
                }
                fileOutputStream.write(buffer, 0, numRead)
            }
            fileOutputStream.flush()
        } catch (e: java.lang.Exception) {
            LogUtil.e(mTag, "Failed to insert media file $e")
            if (url != null) {
                cr.delete(url, null, null)
                url = null
            }
        }
        if (url != null) {
            stringUrl = url.toString()
        }
        return stringUrl
    }

    fun getQQFile(context: Context) {
        //针对Android7以上,配置uri
        /*if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(
                context, "com.tencent.mobileqq",

            )
        }*/
    }
}