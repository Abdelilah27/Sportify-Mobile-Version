package com.app.sportify.network

import com.app.sportify.model.User
import com.app.sportify.model.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AppRetrofitServiceInterface {

    @Headers("Content-Type: application/json")
    @POST("/auth/save_user")
    fun createUser(@Body user: User): Call<UserResponse>
}