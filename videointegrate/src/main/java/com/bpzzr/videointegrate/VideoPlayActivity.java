package com.bpzzr.videointegrate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.source.UrlSource;
import com.bpzzr.videointegrate.control.IPlayerImpl;
import com.bpzzr.videointegrate.databinding.ActivityVideoPlayBinding;
import com.bpzzr.videointegrate.databinding.VcPlayerShowBinding;

public class VideoPlayActivity extends AppCompatActivity {

    private ActivityVideoPlayBinding binding;
    VcPlayerShowBinding showBinding;

    public static void startSelf(Activity activity) {
        Intent intent = new Intent(activity, VideoPlayActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayBinding.inflate(getLayoutInflater());
        //showBinding = VcPlayerShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        /*binding.ar.setAutoPlay(true);
        binding.ar.setSurfaceType(AliyunRenderView.SurfaceType.SURFACE_VIEW);
        UrlSource urlSource = new UrlSource();
        urlSource.setUri("https://xytzq01.oss-cn-shanghai.aliyuncs.com/data/course/course_96/resource/video/5.mp4");
        urlSource.setCacheFilePath(getCacheDir().getAbsolutePath() + "/test/");
        binding.ar.setDataSource(urlSource);
        binding.ar.prepare();

        IPlayerImpl iPlayer = new IPlayerImpl();
        binding.ar.setOnErrorListener(iPlayer);
        binding.ar.setOnPreparedListener(iPlayer);
        binding.ar.setOnInfoListener(iPlayer);
        binding.ar.setOnCompletionListener(iPlayer);
        binding.ar.setOnSeekCompleteListener(iPlayer);
        binding.ar.setOnLoadingStatusListener(iPlayer);
        binding.ar.setOnRenderingStartListener(iPlayer);
        binding.ar.setOnTrackChangedListener(iPlayer);
        binding.ar.setOnSeiDataListener(iPlayer);*/
        /*binding.ar.setOnErrorListener(new IPlayer.OnErrorListener() {
            @Override
            public void onError(ErrorInfo errorInfo) {
                Log.e("error: ", errorInfo.getMsg());
            }
        });
        binding.ar.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                Log.e("onPrepared: ","准备完成");
                //binding.ar.start();
            }
        });*/
    }
}