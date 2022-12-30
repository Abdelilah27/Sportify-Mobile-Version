package com.app.sportify.network

import com.app.sportify.model.utils.UserAuth
import retrofit2.Call
import retrofit2.http.GET

// For request with headers
interface AuthAppRetrofitServiceInterface {

    @GET("/auth/user_auth")
    fun getUserConnected(): Call<UserAuth>
}