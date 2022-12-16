package com.app.entity.network

import com.app.entity.model.ListStadium
import com.app.entity.model.StadiumResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitServiceInterface {
    @GET("terrain/all")
    suspend fun getStadiumList(): Response<ListStadium>

    @Multipart
    @POST("terrain/save")
    fun saveStadium(
        @Part("terrain") terrain: RequestBody,
        @Part img: MultipartBody.Part
    ):
            Call<StadiumResponse>

    // Post without img
    @Multipart
    @POST("terrain/save")
    fun saveStadium(
        @Part("terrain") terrain: RequestBody,
    ):
            Call<StadiumResponse>

    @HTTP(method = "DELETE", path = "terrain/delete/{id}", hasBody = true)
    fun deleteStadium(@Path("id") id: String): Call<ResponseBody>
}
