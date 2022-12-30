package com.app.sportify.repository

import com.app.sportify.model.Role
import com.app.sportify.model.User
import com.app.sportify.model.utils.UserAuth
import com.app.sportify.model.utils.UserLogin
import com.app.sportify.model.utils.UserLoginResponse
import com.app.sportify.model.utils.UserResponse
import com.app.sportify.network.AppRetrofitServiceInterface
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class AppRetrofitServiceRepository @Inject constructor(
    private val appRetrofitServiceInterface: AppRetrofitServiceInterface
) {

    fun createUser(
        user: User
    ): Call<UserResponse> {
        return appRetrofitServiceInterface.createUser(user)
    }


    fun addRoleToUser(
        role: Role
    ): Call<ResponseBody> {
        return appRetrofitServiceInterface.addRoleToUser(role)
    }

    fun login(
        params: UserLogin
    ): Call<UserLoginResponse> {
        return appRetrofitServiceInterface.login(params)
    }

}