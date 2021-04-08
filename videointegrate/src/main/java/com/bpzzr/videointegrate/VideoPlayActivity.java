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

    public static void startSelf(Activity activity) {
        Intent intent = new Intent(activity, VideoPlayActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getLifecycle().addObserver(binding.cvv);


    }
}