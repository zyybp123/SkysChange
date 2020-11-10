package com.bpzzr.audiolibrary.audio

data class AudioEntity(
    var audioCover: String? = null,
    var audioName: String? = null,
    var audioUrl: String? = null,
    var isLocal: Boolean = false,
    var artiest: String? = null,
)