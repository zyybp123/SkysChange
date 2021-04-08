package com.bpzzr.videointegrate.interf;

/**
 * 视频播放状态
 * 准备中、准备完成、播放中、播放暂停
 */
public enum VideoState {
    STATE_PREPARING, STATE_PREPARED, STATE_PLAYING,
    STATE_PAUSE,STATE_AUTO_PAUSE;
}
