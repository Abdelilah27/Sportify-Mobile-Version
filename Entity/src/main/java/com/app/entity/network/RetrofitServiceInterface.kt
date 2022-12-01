package com.app.entity.network

import com.app.entity.model.ListStadium
import okhttp3.MultipartBody
import okhttp3.ResponseBody
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
    suspend fun saveStadium(
        @Part img: MultipartBody.Part,
        @Part terrain: MultipartBody.Part
    ):
            Call<ResponseBody>
}