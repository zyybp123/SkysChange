package com.bpzzr.videointegrate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

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
import com.bpzzr.videointegrate.util.TimeFormater;

/**
 * 标准视频展示层
 */
public class CustomVodPlayerView extends FrameLayout {

    private AliyunRenderView mVcAliRenderView;
    private VcTopTitleAreaBinding titleAreaBinding;
    private VcCenterTipAreaBinding centerTipAreaBinding;
    private VcBottomSeekAreaBinding seekAreaBinding;
    private MediaInfo mMediaInfo;
    private VideoState mState;

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
        titleAreaBinding.getRoot().setVisibility(GONE);

    }

    private void initPlayer() {


        //初始化设置视频播放器
        mVcAliRenderView.setAutoPlay(true);
        mVcAliRenderView.setSurfaceType(AliyunRenderView.SurfaceType.SURFACE_VIEW);

        UrlSource urlSource = new UrlSource();
        urlSource.setUri("https://xytzq01.oss-cn-shanghai.aliyuncs.com/data/course/course_96/resource/video/5.mp4");
        urlSource.setCacheFilePath(getContext().getCacheDir().getAbsolutePath() + "/test/");

        mVcAliRenderView.setDataSource(urlSource);
        mVcAliRenderView.prepare();

        IPlayerImpl iPlayer = new IPlayerImpl() {
            @Override
            public void onInfo(InfoBean infoBean) {
                super.onInfo(infoBean);
                if (infoBean == null) return;
                InfoCode code = infoBean.getCode();
                long extraValue = infoBean.getExtraValue();
                switch (code) {
                    case BufferedPosition:
                        seekAreaBinding.vcSeekBar.setSecondaryProgress((int) extraValue);
                        break;
                    case CurrentPosition:
                        seekAreaBinding.vcSeekBar.setProgress((int) extraValue);
                        seekAreaBinding.vcTvStartTime.setText(TimeFormater.formatMs(extraValue));
                        break;
                }
            }

            @Override
            public void onLoadingBegin() {
                super.onLoadingBegin();
            }

            @Override
            public void onLoadingProgress(int percent, float netSpeed) {
                super.onLoadingProgress(percent, netSpeed);
            }

            @Override
            public void onLoadingEnd() {
                super.onLoadingEnd();
            }

            @Override
            public void onPrepared() {
                super.onPrepared();
                mMediaInfo = mVcAliRenderView.getMediaInfo();
                seekAreaBinding.vcTvEndTime.setText(
                        TimeFormater.formatMs(mMediaInfo.getDuration()));
                seekAreaBinding.vcSeekBar.setMax(mMediaInfo.getDuration());
            }

            @Override
            public void onRenderingStart() {
                super.onRenderingStart();
                centerTipAreaBinding.getRoot().setVisibility(GONE);
            }

            @Override
            public void onCompletion() {
                super.onCompletion();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                super.onError(errorInfo);
            }

            @Override
            public void onSeekComplete() {
                super.onSeekComplete();
            }

            @Override
            public void onSeiData(int i, byte[] bytes) {
                super.onSeiData(i, bytes);
            }

            @Override
            public void onChangedSuccess(TrackInfo trackInfo) {
                super.onChangedSuccess(trackInfo);
            }

            @Override
            public void onChangedFail(TrackInfo trackInfo, ErrorInfo errorInfo) {
                super.onChangedFail(trackInfo, errorInfo);
            }
        };

        mVcAliRenderView.setOnInfoListener(iPlayer);
        mVcAliRenderView.setOnLoadingStatusListener(iPlayer);
        mVcAliRenderView.setOnPreparedListener(iPlayer);
        mVcAliRenderView.setOnCompletionListener(iPlayer);
        mVcAliRenderView.setOnSeekCompleteListener(iPlayer);
        mVcAliRenderView.setOnRenderingStartListener(iPlayer);
        mVcAliRenderView.setOnErrorListener(iPlayer);
        mVcAliRenderView.setOnTrackChangedListener(iPlayer);
        mVcAliRenderView.setOnSeiDataListener(iPlayer);
    }


    public void showPlaying() {
        //显示正常播放状态
    }

    public void uiChange(VideoState state) {
        switch (state) {
            case STATE_PREPARED:
                break;
            case STATE_PREPARING:
                break;
            case STATE_PLAYING_STATE_PAUSE:
                break;
        }

    }


}
