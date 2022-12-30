package com.app.sportify.network

import android.util.Log
import com.app.sportify.model.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        val token = tokenManager.getAccessToken()
        request.addHeader("Authorization", value = "Bearer $token")
        return chain.proceed(request.build())
    }
}