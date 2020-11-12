package com.bpzzr.audiolibrary.audio

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import com.bpzzr.commonlibrary.file.BaseFileEntity
import com.bpzzr.commonlibrary.file.ScanType
import com.bpzzr.commonlibrary.util.LogUtil
import java.util.ArrayList

/**
 * 获取本地保存的音频
 */
class AudioScanner {
    private val mTag = "AudioScanner"
    fun getAudioList(context: Context,
                     selectionArg: Array<String?>?): ArrayList<BaseFileEntity> {
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
            MediaStore.Files.FileColumns.SIZE, path,
            MediaStore.MediaColumns.ARTIST,
            MediaStore.MediaColumns.ALBUM,

        )
        //uri设置
        //Uri uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        var uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        //if (scanType == ScanType.SCAN_AUDIOS) {
        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        //}
        //if (scanType == ScanType.SCAN_VIDEOS) {
        //    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        //}
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


}