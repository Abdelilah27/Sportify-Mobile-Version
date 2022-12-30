package com.app.sportify.repository

import com.app.sportify.model.User
import com.app.sportify.model.UserResponse
import com.app.sportify.network.AppRetrofitServiceInterface
import retrofit2.Call
import javax.inject.Inject

class AppRetrofitServiceRepository @Inject constructor(
    private val appRetrofitServiceInterface: AppRetrofitServiceInterface
) {

    fun createUser(
        user: User
    ): Call<UserResponse> {
        return appRetrofitServiceInterface.createUser(user)
    }

}