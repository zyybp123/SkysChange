package com.bpzzr.commonlibrary.net

import retrofit2.Retrofit




class NetManager private constructor() {

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            //.addConverterFactory()
            .build()
    }

    companion object {
        val instance: NetManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetManager()
        }
    }


}