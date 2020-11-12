package com.bpzzr.commonlibrary.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.util.TypedValue
import androidx.annotation.DimenRes

/**
 * Created by Administrator on 2018/5/14.
 * 尺寸工具类
 */
class DimensionUtil private constructor() {
    /**
     * 根据设备信息获取当前分辨率下指定单位对应的像素大小； px,dip,sp -> px
     *
     * @param context 上下文
     * @param unit    单位 TypeValue.COMPLEX_UNIT_PX:
     * COMPLEX_UNIT_DIP:
     * COMPLEX_UNIT_SP:等
     * @param size    对应单位的大小
     * @return 返回像素大小
     */
    fun getRawSize(context: Context?, unit: Int, size: Float): Float {
        val r: Resources = if (context == null) {
            Resources.getSystem()
        } else {
            context.resources
        }
        return TypedValue.applyDimension(unit, size, r.displayMetrics)
    }

    companion object {
        /**
         * 根据基准设备的宽获取目标设备的宽
         *
         * @param context             上下文
         * @param standardScreenValue 基准设备的屏幕宽度
         * @param standardValue       在基准设备上的值
         * @return 返回在目标设备上的宽
         */
        fun getActualWidth(context: Context, standardScreenValue: Int, standardValue: Int): Int {
            val targetScreenWidth = totalSize(context).x
            return standardValue * targetScreenWidth / standardScreenValue
        }

        /**
         * 获取屏幕的宽高
         *
         * @param context 上下文
         * @return 返回 Point x为宽，y为高
         */
        fun totalSize(context: Context): Point {
            val dm = context.resources.displayMetrics
            val mTotalWidth = dm.widthPixels
            val mTotalHeight = dm.heightPixels
            return Point(mTotalWidth, mTotalHeight)
        }

        /**
         * 根据基准设备的宽获取目标设备的高
         *
         * @param context             上下文
         * @param standardScreenValue 基准设备的屏幕高度
         * @param standardValue       在基准设备上的值
         * @return 返回在目标设备上的高
         */
        fun getActualHeight(context: Context, standardScreenValue: Int, standardValue: Int): Int {
            val targetScreenHeight = totalSize(context).y
            return standardValue * targetScreenHeight / standardScreenValue
        }

        /**
         * dp转px
         *
         * @param context 上下文
         * @param dpValue dp值
         * @return 返回px值
         */
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * sp转px
         *
         * @param context 上下文
         * @param spValue sp值
         * @return 返回px值
         */
        fun sp2px(context: Context, spValue: Float): Int {
            val scale = context.resources.displayMetrics.scaledDensity
            return (spValue * scale + 0.5f).toInt()
        }

        /**
         * px转dp
         *
         * @param context 上下文
         * @param pxValue px值
         * @return 返回dp值
         */
        fun px2dip(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }

        /**
         * 从dimension文件获取出px值
         *
         * @param dimenRes dimension资源
         */
        fun getPx(context: Context, @DimenRes dimenRes: Int): Float {
            return context.resources.getDimension(dimenRes)
        }

        /**
         * 从dimension文件获取出px值
         *
         * @param dimenRes dimension资源
         */
        fun getPxSize(context: Context, @DimenRes dimenRes: Int): Int {
            return context.resources.getDimensionPixelSize(dimenRes)
        }

        /**
         * 从dimension文件获取出px值
         *
         * @param dimenRes dimension资源
         */
        fun getPxOffset(context: Context, @DimenRes dimenRes: Int): Int {
            return context.resources.getDimensionPixelOffset(dimenRes)
        }

        /**
         * 从dimension文件获取出dp值
         *
         * @param dimenRes dimension资源
         */
        fun getDp(context: Context, @DimenRes dimenRes: Int): Int {
            return px2dip(context, context.resources.getDimension(dimenRes))
        }

        fun px2dipF(context: Context, pxValue: Float): Float {
            val scale = context.resources.displayMetrics.density
            return pxValue / scale + 0.5f
        }

        /**
         * 从dimension文件获取出dp值
         *
         * @param dimenRes dimension资源
         */
        fun getDpF(context: Context, @DimenRes dimenRes: Int): Float {
            return px2dipF(context, context.resources.getDimension(dimenRes))
        }

        fun getSp(context: Context, @DimenRes dimenRes: Int): Int {
            return px2sp(context, context.resources.getDimension(dimenRes))
        }

        /**
         * px转sp
         *
         * @param context 上下文
         * @param pxValue px值
         * @return 返回dp值
         */
        fun px2sp(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.scaledDensity
            return (pxValue / scale + 0.5f).toInt()
        }

        /**
         * 获取状态栏高度（单位：px）
         *
         * @return 状态栏高度（单位：px）
         */
        fun getStatusBarHeight(context: Context): Int {
            val resources = context.resources
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return resources.getDimensionPixelSize(resourceId)
        }
    }
}