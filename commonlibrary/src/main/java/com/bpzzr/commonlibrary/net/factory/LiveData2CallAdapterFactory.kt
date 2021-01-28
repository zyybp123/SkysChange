package com.bpzzr.commonlibrary.net.factory

import androidx.lifecycle.LiveData
import com.bpzzr.commonlibrary.util.LogUtil
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveData2CallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type, annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        LogUtil.d(TAG, "rawObservableType = " + rawObservableType.simpleName)
        require(observableType is ParameterizedType) { "resource must be parameterized" }
        val bodyType = getParameterUpperBound(0, observableType)
        LogUtil.d(TAG, "bodyType = $bodyType")
        return null//LiveData2CallAdapter<*>(observableType)
    }

    companion object {
        private const val TAG = "LiveData2CallAdapterFactory"
        fun create(): LiveData2CallAdapterFactory {
            return LiveData2CallAdapterFactory()
        }
    }
}