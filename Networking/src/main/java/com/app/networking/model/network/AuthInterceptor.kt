package com.app.networking.model.network

import android.util.Log
import com.app.networking.repository.RetrofitServiceRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var repository: RetrofitServiceRepository

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = tokenManager.getAccessToken()
        request.addHeader("Authorization", value = "Bearer $token")

        val response = chain.proceed(request.build())
        // Check if the access token has expired
        if (response.code == 401 || response.code == 403) {
            // Use the refresh token to get a new access token
            val refreshToken = tokenManager.getRefreshToken()
            val newAccessToken = refreshToken(refreshToken.toString())
            // Rebuild the request with the new access token and retry
            val newRequest = request.header("Authorization", "Bearer $newAccessToken").build()
            return chain.proceed(newRequest)
        }
        return response
    }


    private fun refreshToken(refreshToken: String): String {
        val call = repository.refreshToken("Bearer $refreshToken")
        val response = call.execute()
        if (response.isSuccessful) {
            val tokenResponse = response.body()!!
            // Store Token
            tokenManager.saveToken(tokenResponse)
            return tokenResponse.access_token
        }
        return ""
    }
}