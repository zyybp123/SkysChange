package com.bpzzr.videointegrate.control;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

/**
 * 控制播放器的底部SeekBar区域
 */
public class BottomSeek extends FrameLayout {
    public BottomSeek(Context context) {
        this(context, null);
    }

    public BottomSeek(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomSeek(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

    }

}
