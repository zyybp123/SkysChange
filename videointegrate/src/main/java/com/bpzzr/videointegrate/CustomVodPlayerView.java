package com.bpzzr.videointegrate;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.bean.InfoBean;
import com.aliyun.player.bean.InfoCode;
import com.aliyun.player.nativeclass.MediaInfo;
import com.aliyun.player.nativeclass.TrackInfo;
import com.aliyun.player.source.UrlSource;
import com.bpzzr.videointegrate.control.IPlayerImpl;
import com.bpzzr.videointegrate.databinding.VcBottomSeekAreaBinding;
import com.bpzzr.videointegrate.databinding.VcCenterTipAreaBinding;
import com.bpzzr.videointegrate.databinding.VcTopTitleAreaBinding;
import com.bpzzr.videointegrate.interf.VideoModel;
import com.bpzzr.videointegrate.interf.VideoState;
import com.bpzzr.videointegrate.util.TimeFormater;
import com.bpzzr.videointegrate.util.VLog;

/**
 * 标准视频展示层
 */
public class CustomVodPlayerView extends FrameLayout implements
        SeekBar.OnSeekBarChangeListener, IPlayerImpl, LifecycleObserver {

    private AliyunRenderView mVcAliRenderView;
    private VcTopTitleAreaBinding titleAreaBinding;
    private VcCenterTipAreaBinding centerTipAreaBinding;
    private VcBottomSeekAreaBinding seekAreaBinding;
    private MediaInfo mMediaInfo;
    private VideoState mState;
    private VideoModel mModel = VideoModel.MODEL_NORMAL;

    public CustomVodPlayerView(Context context) {
        this(context, null);
    }

    public CustomVodPlayerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVodPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.vc_player_show,
                this, false);
        removeAllViews();
        addView(view);
        mVcAliRenderView = view.findViewById(R.id.vc_ali_render_view);
        View topView = view.findViewById(R.id.top_title_area);
        titleAreaBinding = VcTopTitleAreaBinding.bind(topView);
        View centerView = view.findViewById(R.id.center_state_area);
        centerTipAreaBinding = VcCenterTipAreaBinding.bind(centerView);
        View bottomView = view.findViewById(R.id.bottom_seek_area);
        seekAreaBinding = VcBottomSeekAreaBinding.bind(bottomView);
        initView();
        initPlayer();
    }

    private void initView() {
        controlShow(mModel);
        //设置进度监听
        seekAreaBinding.vcSeekBar.setOnSeekBarChangeListener(this);
        //
        OnClickListener playListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                dealPlayState();
            }
        };
        seekAreaBinding.vcIvPlay.setOnClickListener(playListener);
        seekAreaBinding.vcIvPlayBig.setOnClickListener(playListener);
    }

    private void initPlayer() {

        setResource();

        mVcAliRenderView.setOnInfoListener(this);
        mVcAliRenderView.setOnLoadingStatusListener(this);
        mVcAliRenderView.setOnPreparedListener(this);
        mVcAliRenderView.setOnCompletionListener(this);
        mVcAliRenderView.setOnSeekCompleteListener(this);
        mVcAliRenderView.setOnRenderingStartListener(this);
        mVcAliRenderView.setOnErrorListener(this);
        mVcAliRenderView.setOnTrackChangedListener(this);
        mVcAliRenderView.setOnSeiDataListener(this);

    }

    public void setResource() {
        //初始化设置视频播放器
        mVcAliRenderView.setAutoPlay(true);
        mVcAliRenderView.setSurfaceType(AliyunRenderView.SurfaceType.SURFACE_VIEW);

        UrlSource urlSource = new UrlSource();
        urlSource.setUri("https://xytzq01.oss-cn-shanghai.aliyuncs.com/data/course/course_96/resource/video/5.mp4");
        urlSource.setCacheFilePath(getContext().getCacheDir().getAbsolutePath() + "/test/");

        mVcAliRenderView.setDataSource(urlSource);
        mVcAliRenderView.prepare();
        mState = VideoState.STATE_PREPARING;
    }

    private void dealPlayState() {
        if (mState == VideoState.STATE_PLAYING) {
            mState = VideoState.STATE_PAUSE;
            mVcAliRenderView.pause();
            controlPlayButton(mState);
            return;
        }
        if (mState == VideoState.STATE_PAUSE) {
            mState = VideoState.STATE_PLAYING;
            mVcAliRenderView.start();
            controlPlayButton(mState);
        }
    }

    private void controlShow(@NonNull VideoModel model) {
        switch (model) {
            case MODEL_NORMAL:
                titleAreaBinding.getRoot().setVisibility(GONE);
                seekAreaBinding.vcIvPlay.setVisibility(VISIBLE);
                seekAreaBinding.vcLlExtend.setVisibility(GONE);
                seekAreaBinding.vcTvStartTime.setVisibility(View.GONE);
                break;
            case MODEL_FULL_SCREEN:
                //全屏显示顶部标题、底部扩展
                titleAreaBinding.getRoot().setVisibility(VISIBLE);
                seekAreaBinding.vcIvPlay.setVisibility(GONE);
                seekAreaBinding.vcLlExtend.setVisibility(VISIBLE);
                seekAreaBinding.vcTvStartTime.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setTime(@NonNull VideoModel model, long currentTime) {
        if (mMediaInfo == null) return;
        String st = TimeFormater.formatMs(currentTime);
        String et = TimeFormater.formatMs(mMediaInfo.getDuration());
        switch (model) {
            case MODEL_NORMAL:
                seekAreaBinding.vcTvEndTime.setText(String.format("%s/%s", st, et));
                break;
            case MODEL_FULL_SCREEN:
                seekAreaBinding.vcTvStartTime.setText(st);
                seekAreaBinding.vcTvEndTime.setText(et);
                break;
        }
    }

    private void controlPlayButton(@NonNull VideoState state) {
        if (state == VideoState.STATE_PLAYING) {
            seekAreaBinding.vcIvPlay.setImageResource(R.drawable.vc_ic_pause);
            seekAreaBinding.vcIvPlayBig.setImageResource(R.drawable.vc_ic_pause);
        } else {
            seekAreaBinding.vcIvPlay.setImageResource(R.drawable.vc_ic_play);
            seekAreaBinding.vcIvPlayBig.setImageResource(R.drawable.vc_ic_play);
        }

    }

    private void showLoading(int percent, float netSpeed) {
        centerTipAreaBinding.getRoot().setVisibility(VISIBLE);
        centerTipAreaBinding.vcPbLoading.setVisibility(View.VISIBLE);
        centerTipAreaBinding.vcLoadTip.setText(String.format(
                "已缓冲%s%%, %s kbps", percent, netSpeed));
    }

    private void hideLoading(String tip) {
        if (TextUtils.isEmpty(tip)) {
            centerTipAreaBinding.getRoot().setVisibility(GONE);
            return;
        }
        centerTipAreaBinding.getRoot().setVisibility(View.VISIBLE);
        centerTipAreaBinding.vcPbLoading.setVisibility(View.GONE);
        centerTipAreaBinding.vcLoadTip.setText(tip);
    }

    private void showRetry() {

    }

    private void setProgress(InfoBean infoBean) {
        if (infoBean == null) return;
        InfoCode code = infoBean.getCode();
        long extraValue = infoBean.getExtraValue();
        switch (code) {
            case BufferedPosition:
                seekAreaBinding.vcSeekBar.setSecondaryProgress((int) extraValue);
                break;
            case CurrentPosition:
                seekAreaBinding.vcSeekBar.setProgress((int) extraValue);
                setTime(mModel, extraValue);
                break;
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            //用户拖拽，更新进度文字
            setTime(mModel, progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mState != VideoState.STATE_PREPARING) {
            //停止拖拽，可以seek
            mVcAliRenderView.seekTo(seekBar.getProgress(), IPlayer.SeekMode.Accurate);
        }
    }

    @Override
    public void onInfo(InfoBean infoBean) {
        //VLog.d("onInfo: " + infoBean);
        if (infoBean != null) {
            VLog.d("info: " + infoBean.getCode()
                    + ", msg: " + infoBean.getExtraMsg()
                    + ", value:" + infoBean.getExtraValue());
        }
        //根据infoCode设置进度的更新
        setProgress(infoBean);
    }

    @Override
    public void onLoadingBegin() {
        VLog.d("onLoadingBegin()");
    }

    @Override
    public void onLoadingProgress(int percent, float netSpeed) {
        VLog.d("onLoadingProgress(percent=" + percent + ", netSpeed=" + netSpeed);
        showLoading(percent, netSpeed);
    }

    @Override
    public void onLoadingEnd() {
        VLog.d("onLoadingEnd()");
        hideLoading(null);
    }

    @Override
    public void onPrepared() {
        VLog.d("onPrepared(...准备完成...)");
        mState = VideoState.STATE_PREPARED;
        //获取视频信息
        mMediaInfo = mVcAliRenderView.getMediaInfo();
        seekAreaBinding.vcSeekBar.setMax(mMediaInfo.getDuration());
    }

    @Override
    public void onRenderingStart() {
        VLog.d("onRenderingStart(...开始渲染...)");
        mState = VideoState.STATE_PLAYING;
        hideLoading(null);
        controlPlayButton(mState);
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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (mState == VideoState.STATE_AUTO_PAUSE) {
            //因生命周期导致的暂停，回来时重新播放
            mVcAliRenderView.start();
            mState = VideoState.STATE_PLAYING;
            controlPlayButton(mState);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (mState == VideoState.STATE_PLAYING) {
            //播放中退出到后台或其它原因，暂停播放
            mVcAliRenderView.pause();
            mState = VideoState.STATE_AUTO_PAUSE;
            controlPlayButton(mState);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (mVcAliRenderView != null) {
            mVcAliRenderView.stop();
            mVcAliRenderView.release();
            mVcAliRenderView = null;
        }
    }
}
