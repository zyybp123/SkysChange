package com.bpzzr.commonlibrary.net.factory

import androidx.lifecycle.LiveData
import com.bpzzr.commonlibrary.util.LogUtil
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveData2CallAdapter<R>(private val responseType: Type) : CallAdapter<R, LiveData<R>> {

    companion object {
        private const val TAG = "LiveData2CallAdapter"
    }

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<R> {
        return object : LiveData<R>() {
            private val started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    //确保执行一次
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            LogUtil.e(TAG, "onResponse:" + response.message())
                            if (response.isSuccessful) {
                                val body = response.body()
                                LogUtil.e(TAG, "body: $body")
                                postValue(body)
                            } else {
                                postValue(null)
                            }
                        }

                        override fun onFailure(call: Call<R>, t: Throwable) {
                            LogUtil.e(TAG, "error:$t")
                            postValue(null)
                        }
                    })
                }
            }
        }
    }
}