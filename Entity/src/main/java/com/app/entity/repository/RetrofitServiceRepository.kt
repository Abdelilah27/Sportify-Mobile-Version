package com.app.entity.repository

import com.app.entity.model.ListStadium
import com.app.entity.model.Stadium
import com.app.entity.model.StadiumResponse
import com.app.entity.network.RetrofitServiceInterface
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class RetrofitServiceRepository @Inject constructor(private val retrofitServiceInterface: RetrofitServiceInterface) {

    suspend fun getStadiumList(): Response<ListStadium> {
        return retrofitServiceInterface.getStadiumList()
    }

    suspend fun saveStadium(stadium: Stadium): Call<StadiumResponse> {
        return retrofitServiceInterface.saveStadium(stadium)
    }
}