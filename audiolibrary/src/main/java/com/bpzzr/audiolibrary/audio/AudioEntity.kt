package com.bpzzr.audiolibrary.audio

import android.net.Uri
import java.time.Duration

data class AudioEntity(
    var audioCover: String? = null,
    var audioName: String? = null,
    var audioUrl: String? = null,
    var audioUri: Uri? = null,
    var isLocal: Boolean = false,
    var artiest: String? = null,
    var currentProgress: Int = 0,
    var duration: Int = 0,
    var dateModify: Long = 0,
    var test: AudioControlEntity? = null
)