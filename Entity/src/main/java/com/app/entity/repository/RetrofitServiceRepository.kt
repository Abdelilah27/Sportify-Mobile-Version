package com.app.entity.repository

import com.app.entity.model.ListStadium
import com.app.entity.model.StadiumResponse
import com.app.entity.network.RetrofitServiceInterface
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class RetrofitServiceRepository @Inject constructor(private val retrofitServiceInterface: RetrofitServiceInterface) {

    suspend fun getStadiumList(): Response<ListStadium> {
        return retrofitServiceInterface.getStadiumList()
    }

    fun saveStadium(
        terrain: RequestBody
    ): Call<StadiumResponse> {
        return retrofitServiceInterface.saveStadium(terrain)
    }
}