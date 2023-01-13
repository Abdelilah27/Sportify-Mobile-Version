package com.app.networking.repository

import com.app.networking.api.AuthRetrofitServiceInterface
import com.app.networking.model.entity.ListStadium
import com.app.networking.model.entity.response.StadiumResponse
import com.app.networking.model.user.Seances
import com.app.networking.model.user.UserAuth
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

class AuthRetrofitServiceRepository @Inject constructor(
    private val authRetrofitServiceInterface: AuthRetrofitServiceInterface
) {
    fun getUserConnected(): Call<UserAuth> =
        authRetrofitServiceInterface.getUserConnected()


    // Entity Area
    fun getStadiumList(): Call<ListStadium> {
        return authRetrofitServiceInterface.getStadiumList()
    }

    fun saveStadium(
        terrain: RequestBody,
        img: MultipartBody.Part
    ): Call<StadiumResponse> {
        return authRetrofitServiceInterface.saveStadium(terrain, img)
    }

    fun saveStadium(
        terrain: RequestBody,
    ): Call<StadiumResponse> {
        return authRetrofitServiceInterface.saveStadium(terrain)
    }

    fun deleteStadium(id: String): Call<ResponseBody> {
        return authRetrofitServiceInterface.deleteStadium(id)
    }

    fun getSeanceByStadium(id: String): Call<Seances> {
        return authRetrofitServiceInterface.getSeanceByStadium(id)
    }
}