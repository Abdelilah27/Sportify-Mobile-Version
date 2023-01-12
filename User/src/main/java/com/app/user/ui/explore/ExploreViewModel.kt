package com.app.user.ui.explore

import android.util.Log
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: AuthRetrofitServiceRepository,
) : ViewModel() {
    val liveUserAuthFlow: MutableLiveData<NetworkResult<UserAuth>> = MutableLiveData()
    val liveDataFlow: MutableLiveData<NetworkResult<ListStadium>> = MutableLiveData()

    private val _entities = MutableLiveData<List<Entity>>()
    val entities: LiveData<List<Entity>> = _entities


    private val _liveUserData = MutableLiveData<UserAuth>(UserAuth())
    val liveUser: LiveData<UserAuth> = _liveUserData


    suspend fun getAuthUser() {
        val call: Call<UserAuth> = repository.getUserConnected()
        call.enqueue(object : Callback<UserAuth> {
            override fun onResponse(
                call: Call<UserAuth>, response: Response<UserAuth>
            ) {
                if (response.isSuccessful) {
                    _liveUserData.postValue(response.body())
                    liveUserAuthFlow.postValue(NetworkResult.Success(response.body()))
                } else {
                    liveUserAuthFlow.postValue(
                        NetworkResult.Error(
                            response.body().toString()
                        )
                    )
                }

            }

            override fun onFailure(call: Call<UserAuth>, t: Throwable) {
                liveUserAuthFlow.postValue(NetworkResult.Error("Error"))
            }
        })
    }


    suspend fun getEntitiesList(): ArrayList<ListStadium> {
        liveDataFlow.postValue(NetworkResult.Loading())
        val stadiums = ArrayList<ListStadium>()
        viewModelScope.launch {
            val call: Call<ListStadium> = repository.getStadiumList()
            call.enqueue(object : Callback<ListStadium> {
                override fun onResponse(call: Call<ListStadium>, response: Response<ListStadium>) {
                    if (response.isSuccessful) {
                        extractEntities(response)
                        liveDataFlow.postValue(NetworkResult.Success(response.body()))
                    } else {
                        liveDataFlow.postValue(NetworkResult.Error(response.body().toString()))
                    }
                }

                override fun onFailure(call: Call<ListStadium>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                    liveDataFlow.postValue(NetworkResult.Error("Error"))
                }

            })
        }
        return stadiums
    }

    private fun extractEntities(response: Response<ListStadium>) {
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
        _entities.postValue(distinctEntities)
    }


}