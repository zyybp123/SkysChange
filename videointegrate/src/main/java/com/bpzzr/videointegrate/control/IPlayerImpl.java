package com.bpzzr.videointegrate.control;

import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.bean.InfoBean;
import com.aliyun.player.nativeclass.TrackInfo;
import com.bpzzr.videointegrate.util.VLog;

/**
 * 实现必要的状态监听接口
 */
public interface IPlayerImpl extends
        IPlayer.OnInfoListener,
        IPlayer.OnPreparedListener,
        IPlayer.OnLoadingStatusListener,
        IPlayer.OnErrorListener,
        IPlayer.OnCompletionListener,
        IPlayer.OnSeekCompleteListener,
        IPlayer.OnTrackChangedListener,
        IPlayer.OnRenderingStartListener,
        IPlayer.OnSeiDataListener {



}
