package com.bpzzr.commonlibrary

import java.util.*

/**
 * 执行定时任务
 */
class PeriodicUtil
/**
 * 执行周期性周期任务
 *
 * @param task   执行内容
 * @param period 周期
 */(private val task: Task, private val delayed: Int, private val period: Int) {
    private var progressUpdateTask: TimerTask? = null
    private var isSchedule = false

    fun stop() {
        if (!isSchedule) {
            return
        }
        if (progressUpdateTask != null) {
            progressUpdateTask!!.cancel()
            isSchedule = false
        }
    }

    fun start() {
        if (isSchedule) {
            return
        }
        val timer = Timer()
        progressUpdateTask = object : TimerTask() {
            override fun run() {
                task.execute()
            }
        }
        timer.schedule(progressUpdateTask, delayed.toLong(), period.toLong())
        isSchedule = true
    }

    interface Task {
        /**
         * 执行周期任务，注意该方法不在主线程执行，不能在其中访问 UI 控件
         */
        fun execute()
    }
}