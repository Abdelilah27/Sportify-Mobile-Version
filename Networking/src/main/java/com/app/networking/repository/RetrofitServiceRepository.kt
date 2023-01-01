package com.app.networking.repository

import com.app.networking.api.RetrofitServiceInterface
import com.app.networking.model.app.Role
import com.app.networking.model.app.UserLogin
import com.app.networking.model.app.response.UserLoginResponse
import com.app.networking.model.app.response.UserResponse
import com.app.networking.model.user.User
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

class RetrofitServiceRepository @Inject constructor(
    private val retrofitServiceInterface: RetrofitServiceInterface
) {

    fun createUser(
        user: User
    ): Call<UserResponse> {
        return retrofitServiceInterface.createUser(user)
    }


    fun addRoleToUser(
        role: Role
    ): Call<ResponseBody> {
        return retrofitServiceInterface.addRoleToUser(role)
    }

    fun login(
        params: UserLogin
    ): Call<UserLoginResponse> {
        return retrofitServiceInterface.login(params)
    }

}