package com.app.entity.repository

import com.app.entity.network.RetrofitServiceInterface
import javax.inject.Inject

class RetrofitServiceRepository @Inject constructor(private val retrofitServiceInterface: RetrofitServiceInterface) {

//    suspend fun getAllStadiums(): Response<ListCategory> {
//        return retrofitServiceInterface.getCategoriesFromAPI()
//    }
}