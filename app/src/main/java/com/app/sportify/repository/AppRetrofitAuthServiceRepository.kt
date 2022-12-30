package com.app.sportify.repository

import com.app.sportify.model.utils.UserAuth
import com.app.sportify.network.AuthAppRetrofitServiceInterface
import retrofit2.Call
import javax.inject.Inject

class AppRetrofitAuthServiceRepository @Inject constructor(
    private val authAppRetrofitServiceInterface: AuthAppRetrofitServiceInterface
) {
    fun getUserConnected(): Call<UserAuth> =
        authAppRetrofitServiceInterface.getUserConnected()
}