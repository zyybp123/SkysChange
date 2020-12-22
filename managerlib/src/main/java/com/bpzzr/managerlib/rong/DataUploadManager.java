package com.bpzzr.managerlib.rong;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * 将本地数据同步至服务器
 */
public class DataUploadManager extends Worker {
    public DataUploadManager(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {

        // Do the work here--in this case, upload the images.
        uploadData();

        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }

    private void uploadData() {

    }

}
