package com.bpzzr.commonlibrary

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * 建立通知管理
 * 1、按组发送通知
 * 2、单独发送通知
 *
 */
class NotificationUtil {


    companion object {
        /**
         * 创建通知通道
         * Create the NotificationChannel, but only on API 26+ because
         * the NotificationChannel class is new and not in the support library
         */
        fun createNotificationChannel(
            context: Context,
            channelName: String,
            channelDesc: String,
            channelId: String
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(
                    channelId, channelName, importance
                ).apply {
                    description = channelDesc
                    setShowBadge(false)
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        /**
         * 创建默认的通知
         * 配置通知要启动的Activity
        val intent = Intent(this, AlertDetails::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
         */
        fun createDefault(context: Context, channelId: String) {
            // Create an explicit intent for an Activity in your app
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            /*val hangIntent = Intent()
            hangIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            hangIntent.setClass(this, MainActivity::class.java)
            val hangPendingIntent =
                PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT)
            builder.setFullScreenIntent(hangPendingIntent, true)*/
            // Set the intent that will fire when the user taps the notification
            //.setContentIntent(pendingIntent)
            //.setAutoCancel(true)
            // notificationId is a unique int for each notification that you must define
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(100, builder.build())
            }
        }

        fun createProgress(context: Context, channelId: String) {
            val builder = NotificationCompat.Builder(
                context, channelId
            ).apply {
                setContentTitle("Picture Download")
                setContentText("Download in progress")
                setSmallIcon(android.R.drawable.ic_notification_overlay)
                priority = NotificationCompat.PRIORITY_LOW
            }
            val max = 100
            var current = 0
            NotificationManagerCompat.from(context).apply {
                // Issue the initial notification with zero progress
                builder.setProgress(max, current, false)
                notify(101, builder.build())

                // Do the job here that tracks the progress.
                // Usually, this should be in a
                // worker thread
                // To show progress, update PROGRESS_CURRENT and update the notification with:
                // builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
                // notificationManager.notify(notificationId, builder.build());

                // When done, update the notification one more time to remove the progress bar
                //builder.setContentText("Download complete")
                //    .setProgress(0, 0, false)
                val job = GlobalScope.launch {
                    repeat(1000) { i ->
                        //println("job: I'm sleeping $i ...")
                        LogUtil.e(TAG, "job: I'm sleeping $current ...")
                        delay(500L)
                        builder.setProgress(100, current++, false)
                        notify(101, builder.build())
                    }
                }
                notify(101, builder.build())
            }
        }
    }
}