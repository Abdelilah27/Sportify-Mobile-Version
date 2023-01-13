package com.app.networking.api

import com.app.networking.model.entity.ListStadium
import com.app.networking.model.entity.response.StadiumResponse
import com.app.networking.model.user.UserAuth
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


// Token Need
interface AuthRetrofitServiceInterface {
    @GET("/SPORTIFYAUTHENTIFICATION/auth/user_auth")
    fun getUserConnected(): Call<UserAuth>


    // Entity Area
    @GET("/SPORTIFYENTITY/terrain/all")
    fun getStadiumList(): Call<ListStadium>

    // Post with img
    @Multipart
    @POST("/SPORTIFYENTITY/terrain/save")
    fun saveStadium(
        @Part("terrain") terrain: RequestBody,
        @Part img: MultipartBody.Part
    ):
            Call<StadiumResponse>

    // Post without img
    @Multipart
    @POST("/SPORTIFYENTITY/terrain/save")
    fun saveStadium(
        @Part("terrain") terrain: RequestBody,
    ):
            Call<StadiumResponse>

    @HTTP(method = "DELETE", path = "/SPORTIFYENTITY/terrain/delete/{id}", hasBody = true)
    fun deleteStadium(@Path("id") id: String): Call<ResponseBody>

    // user api
    
}