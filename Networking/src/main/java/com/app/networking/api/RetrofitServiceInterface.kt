package com.app.networking.api

import com.app.networking.model.app.Role
import com.app.networking.model.app.UserLogin
import com.app.networking.model.app.response.UserLoginResponse
import com.app.networking.model.app.response.UserResponse
import com.app.networking.model.user.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


// No Token Need
interface RetrofitServiceInterface {
    @Headers("Content-Type: application/json")
    @POST("/auth/save_user")
    fun createUser(@Body user: User): Call<UserResponse>

    @Headers("Content-Type: application/json")
    @POST("/auth/addRoleToUser")
    fun addRoleToUser(@Body role: Role): Call<ResponseBody>


    @Headers("Content-Type: application/json")
    @POST("/login")
    fun login(@Body params: UserLogin): Call<UserLoginResponse>
}