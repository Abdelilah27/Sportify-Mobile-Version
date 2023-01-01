package com.app.networking.api

import com.app.networking.model.app.UserLogin
import com.app.networking.model.app.response.TokenResponse
import com.app.networking.model.app.response.UserResponse
import com.app.networking.model.user.Role
import com.app.networking.model.user.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


// No Token Need
interface RetrofitServiceInterface {
    @Headers("Content-Type: application/json")
    @POST("/SPORTIFYAUTHENTIFICATION/auth/save_user")
    fun createUser(@Body user: User): Call<UserResponse>

    @Headers("Content-Type: application/json")
    @POST("/SPORTIFYAUTHENTIFICATION/auth/addRoleToUser")
    fun addRoleToUser(@Body role: Role): Call<ResponseBody>


    @Headers("Content-Type: application/json")
    @POST("/SPORTIFYAUTHENTIFICATION/login")
    fun login(@Body params: UserLogin): Call<TokenResponse>

    // Refresh token
    @GET("/SPORTIFYAUTHENTIFICATION/auth/refreshToken")
    fun refreshToken(@Header("refresh_token") refreshToken: String): Call<TokenResponse>

}