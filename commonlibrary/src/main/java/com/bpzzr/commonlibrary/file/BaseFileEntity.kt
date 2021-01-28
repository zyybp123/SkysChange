package com.bpzzr.commonlibrary.file

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 记录本地文件查询的详情
 */
@Parcelize
data class BaseFileEntity(
    //文件id
    var fileId: String? = null,
    //文件名
    var name: String? = null,
    //文件名,带后缀(Android系统内displayName为带后缀名称)
    var nameSuffix: String? = null,
    //文件类型
    var type: String? = null,
    //文件路径
    var data: String? = null,
    //文件相对路径
    var relativePath: String? = null,
    //文件的最后修改日期
    var dateModify: Long? = 0,
    //文件的大小
    var size: Long? = 0,
    //文件是否选中的标识
    var isSelect: Boolean = false
) : Parcelable {
    override fun toString(): String {
        return nameSuffix!!
    }
}