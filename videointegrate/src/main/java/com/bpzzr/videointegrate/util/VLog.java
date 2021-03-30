package com.bpzzr.videointegrate.util;

import android.util.Log;

import com.bpzzr.videointegrate.BuildConfig;

public class VLog {
    private static final String TAG = VLog.class.getSimpleName();
    public static boolean isShowLog = BuildConfig.DEBUG;

    public static void d(Object object, String msg) {
        if (isShowLog) {
            Log.d(object.getClass().getSimpleName(), msg);
        }
    }

    public static void e(Object object, String msg) {
        if (isShowLog) {
            Log.e(object.getClass().getSimpleName(), msg);
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (isShowLog) {
            Log.d(tag, msg);
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (isShowLog) {
            Log.e(tag, msg);
        }
    }

    public static String getLogTag(Object o) {
        return o == null ? TAG : o.getClass().getSimpleName();
    }
}
