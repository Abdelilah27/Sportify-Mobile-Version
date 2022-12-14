package com.app.entity.network

import com.app.entity.model.ListStadium
import com.app.entity.model.StadiumResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RetrofitServiceInterface {
    @GET("terrain/all")
    suspend fun getStadiumList(): Response<ListStadium>

    @Multipart
    @POST("terrain/save")
    fun saveStadium(
        @Part("terrain") terrain: RequestBody
    ):
            Call<StadiumResponse>
}
