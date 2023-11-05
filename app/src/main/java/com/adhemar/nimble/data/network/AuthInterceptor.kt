package com.adhemar.nimble.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private  val apiService: ApiService): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("Not yet implemented")
    }

}