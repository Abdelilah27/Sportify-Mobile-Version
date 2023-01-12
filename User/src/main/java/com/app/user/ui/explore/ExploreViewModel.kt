package com.app.user.ui.explore

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.networking.api.AuthRetrofitServiceInterface
import com.app.networking.model.entity.ListStadium
import com.app.networking.model.user.UserAuth
import com.app.networking.repository.AuthRetrofitServiceRepository
import com.app.user.model.Entity
import com.app.user.utils.NetworkResult
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@SuppressLint("MissingPermission")
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: AuthRetrofitServiceRepository,
    @ApplicationContext private val context: Context,


) : ViewModel() {
    // Progress Bar
    val liveDataFlow: MutableLiveData<NetworkResult<String>> = MutableLiveData()

    private val _entities = MutableLiveData<List<Entity>>()
    val entities: LiveData<List<Entity>> = _entities

    private val _liveUserData = MutableLiveData<UserAuth>(UserAuth())
    val liveUser: LiveData<UserAuth> = _liveUserData


    // LiveData to hold the current city name
    val currentLocation: MutableLiveData<Location> = MutableLiveData()


    init {
        liveDataFlow.postValue(NetworkResult.Loading())
    }

    suspend fun getAuthUser() {
        val call: Call<UserAuth> = repository.getUserConnected()
        call.enqueue(object : Callback<UserAuth> {
            override fun onResponse(
                call: Call<UserAuth>, response: Response<UserAuth>
            ) {
                if (response.isSuccessful) {
                    _liveUserData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<UserAuth>, t: Throwable) {
            }
        })
    }

    suspend fun getEntitiesList(): ArrayList<ListStadium> {
        val stadiums = ArrayList<ListStadium>()
        viewModelScope.launch {
            val call: Call<ListStadium> = repository.getStadiumList()
            call.enqueue(object : Callback<ListStadium> {
                override fun onResponse(call: Call<ListStadium>, response: Response<ListStadium>) {
                    if (response.isSuccessful) {
                        Log.d("response", response.body().toString())
                        extractEntities(response)
                    }
                }

                override fun onFailure(call: Call<ListStadium>, t: Throwable) {
                    liveDataFlow.postValue(NetworkResult.Error("Error"))
                }

            })
        }
        return stadiums
    }

    private fun extractEntities(response: Response<ListStadium>) {
        try {
            val entitiesList = mutableListOf<Entity>()
            // Prepare EntitiesList
            response.body()?.forEach { it ->
                entitiesList.add(
                    Entity(
                        name = it.entity,
                        location = it.location,
                        imgFileName = it.imgFileName
                    )
                )

            }
            // Remove duplicate entities
            val distinctEntities = entitiesList.distinctBy { it.name }
            Log.d("distinctEntities", distinctEntities.toString())

            _entities.postValue(distinctEntities)
            liveDataFlow.postValue(NetworkResult.Success("Success"))

        } catch (e: Exception) {
            liveDataFlow.postValue(NetworkResult.Error("Error"))
        }
    }


}