package com.bpzzr.videointegrate.control;

import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.bean.InfoBean;
import com.aliyun.player.nativeclass.TrackInfo;
import com.bpzzr.videointegrate.util.VLog;

/**
 * 实现必要的状态监听接口
 */
public class IPlayerImpl implements
        IPlayer.OnInfoListener,
        IPlayer.OnPreparedListener,
        IPlayer.OnLoadingStatusListener,
        IPlayer.OnErrorListener,
        IPlayer.OnCompletionListener,
        IPlayer.OnSeekCompleteListener,
        IPlayer.OnTrackChangedListener,
        IPlayer.OnRenderingStartListener,
        IPlayer.OnSeiDataListener {

    @Override
    public void onInfo(InfoBean infoBean) {
        //VLog.d("onInfo: " + infoBean);
        if (infoBean != null){
            VLog.d("info: " + infoBean.getCode()
                    + ", msg: " + infoBean.getExtraMsg()
                    + ", value:" + infoBean.getExtraValue());
        }
        //根据infoCode设置进度的更新
    }

    @Override
    public void onLoadingBegin() {
        VLog.d("onLoadingBegin()");
    }

    @Override
    public void onLoadingProgress(int percent, float netSpeed) {
        VLog.d("onLoadingProgress(percent=" + percent + ", netSpeed=" + netSpeed);
    }

    @Override
    public void onLoadingEnd() {
        VLog.d("onLoadingEnd()");
    }

    @Override
    public void onPrepared() {
        VLog.d("onPrepared(...准备完成...)");
    }

    @Override
    public void onRenderingStart() {
        VLog.d("onRenderingStart(...开始渲染...)");
    }

    @Override
    public void onCompletion() {
        VLog.d("onCompletion(...播放结束...)");
    }

    @Override
    public void onError(ErrorInfo errorInfo) {
        //VLog.d("onError: " + errorInfo);
        if (errorInfo != null) {
            VLog.d("err code: " + errorInfo.getCode()
                    + ", msg: " + errorInfo.getMsg()
                    + ", extra:" + errorInfo.getExtra());
        }

    }


    @Override
    public void onSeekComplete() {
        VLog.d("onSeekComplete(...拖拽到结束...)");
    }


    @Override
    public void onSeiData(int i, byte[] bytes) {
        //
    }

    @Override
    public void onChangedSuccess(TrackInfo trackInfo) {
        //切换流
    }

    @Override
    public void onChangedFail(TrackInfo trackInfo, ErrorInfo errorInfo) {

    }

}
