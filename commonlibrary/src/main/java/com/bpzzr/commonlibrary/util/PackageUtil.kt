package com.bpzzr.commonlibrary.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.annotation.NonNull
import androidx.annotation.Nullable


class PackageUtil private constructor() {
    companion object {
        fun getVersionCode(context: Context): Int {
            return getPackageInfo(context)?.versionCode!!
        }

        @Nullable
        private fun getPackageInfo(context: Context): PackageInfo? {
            val pi: PackageInfo?
            try {
                val pm: PackageManager = context.packageManager
                pi = pm.getPackageInfo(context.packageName, PackageManager.GET_CONFIGURATIONS)
                return pi
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}