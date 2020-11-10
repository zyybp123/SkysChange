package com.bpzzr.commonlibrary.file

class FileType {
    companion object {
        /**
         * 纯文本
         */
        const val TEXT_PLAIN = "text/plain" // （纯文本）

        /**
         * （PDF文档）
         */
        const val PDF = "application/pdf"

        /**
         * （Microsoft Word文件）
         */
        const val WORD = "application/msword"
        const val DOC_X = "application/vnd.openxmlformats-officedocument" +
                ".wordprocessingml.document"
        const val DOT_X = "application/vnd.openxmlformats-officedocument" +
                ".wordprocessingml.template"

        /**
         * (Microsoft Excel文件)
         */
        const val EXCEL = "application/vnd.ms-excel"
        const val XLS_X = "application/vnd.openxmlformats-officedocument" +
                ".spreadsheetml.sheet"
        const val XLT_X = "application/vnd.openxmlformats-officedocument" +
                ".spreadsheetml.template"

        /**
         * (Microsoft PowerPoint文件)
         */
        const val PPT = "application/vnd.ms-powerpoint"
        const val PPT_X = "application/vnd.openxmlformats-officedocument" +
                ".presentationml.presentation"
        const val POT_X = "vnd.openxmlformats-officedocument" +
                ".presentationml.template"
        const val PPS_X = "vnd.openxmlformats-officedocument" +
                ".presentationml.slideshow"

        /**
         * Video avi,flv,swf,mp4
         */
        const val AVI = "video/x-msvideo"
        const val FLV = "video/x-flv"
        const val SWF = "application/x-shockwave-flash"
        const val MP4 = "video/mp4"

        /**
         * Image jpeg、png、jpg
         */
        const val JPG = "image/jpeg"
        const val PNG = "image/png"
        val ALL_LIST = arrayOf(
            WORD, DOC_X, EXCEL, XLS_X, PPT, PPT_X, PDF, AVI, FLV, SWF, MP4, JPG, PNG
        )
        val DOCUMENT_LIST_ALL = arrayOf(
            WORD, DOC_X, EXCEL, XLS_X, PPT, PPT_X, PDF
        )
        val BP_DOCUMENT_LIST = arrayOf(
            WORD, DOC_X, PPT, PPT_X, PDF
        )
        val VIDEO_LIST = arrayOf(
            MP4
        )
        val IMAGE_LIST = arrayOf(
            JPG, PNG
        )

        /**
         * （使用HTTP的POST方法提交的表单）
         */
        const val FORM = "application/x-www-form-urlencoded"

        /**
         * （同上，但主要用于表单提交时伴随文件上传的场合）
         */
        const val FORM_DATA = "multipart/form-data"

        //下载的文件
        const val DOWNLOAD = "/Download"

        //QQ接收的文件/sdcard/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv
        const val QQ_FILE = "/tencent/QQfile_recv"
        const val TIM_FILE = "/tencent/TIMfile_recv"

        //WX接收的文件
        const val WX_FILE = "/tencent/MicroMsg/Download"
        const val WX_FILE_2 = "/tencent/MicroMsg/WeiXin"

        //百度云下载的文件
        const val BD_FILE = "/BaiduNetdisk"

        //UC浏览器
        const val UC_FILE = "/UCDownloads"
    }
}