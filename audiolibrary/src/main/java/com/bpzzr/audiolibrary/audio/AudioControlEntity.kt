package com.bpzzr.audiolibrary.audio

/**
 * 音频播放控制数据实体
 * 指令，播放总时长
 */
data class AudioControlEntity(
    var cammand: ControlCommand,
    var duration: Int? = 0,
    var errorWhat: String? = null,
    var progress: Int = 0,
    var createTime: Long = 0,
) {


}

enum class ControlCommand {
    START, PREPARED, ERROR, PROGRESS,
    PLAY, PAUSE, NEXT, PREVIOUS,
}