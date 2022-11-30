package com.app.entity.network

import com.app.entity.model.ListStadium
import com.app.entity.model.Stadium
import com.app.entity.model.StadiumResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitServiceInterface {
    @GET("terrain/all")
    suspend fun getStadiumList(): Response<ListStadium>

    @Headers("Content-Type:application/json")
    @POST("terrain/save")
    suspend fun saveStadium(@Body params: Stadium): Call<StadiumResponse>
}