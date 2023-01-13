package com.app.user.ui.matchs

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.networking.model.entity.ListStadium
import com.app.networking.model.entity.Stadium
import com.app.networking.model.reservation.ReservationByUserResponse
import com.app.networking.repository.AuthRetrofitServiceRepository
import com.app.user.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MatchsViewModel @Inject constructor(
    private val repository: AuthRetrofitServiceRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    val liveDataFlow: MutableLiveData<NetworkResult<ReservationByUserResponse>> = MutableLiveData()
    private val terrainIds = ArrayList<String>()
    private val _stadiums = MutableLiveData<Stadium>()
    val stadiumsData: LiveData<Stadium> = _stadiums

    suspend fun getReservations() {
        liveDataFlow.postValue(NetworkResult.Loading())
        viewModelScope.launch {
            val call: Call<ReservationByUserResponse> = repository.getEventReservedByUser()
            call.enqueue(object : Callback<ReservationByUserResponse> {
                override fun onResponse(
                    call: Call<ReservationByUserResponse>,
                    response: Response<ReservationByUserResponse>
                ) {
                    if (response.isSuccessful) {
                        getIds(response.body()!!)
                        runBlocking {
                            getStadiumList()
                        }
                        liveDataFlow.postValue(NetworkResult.Success(response.body()))
                    } else {
                        liveDataFlow.postValue(NetworkResult.Error(response.body().toString()))
                    }
                }

                override fun onFailure(call: Call<ReservationByUserResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                    liveDataFlow.postValue(NetworkResult.Error("Error"))
                }

            })
        }
    }

    private fun getIds(body: ReservationByUserResponse) {
        body.forEach {
            terrainIds.add(it.terrainId.toString())
        }
    }

    suspend fun getStadiumList() {
        viewModelScope.launch {
            val call: Call<ListStadium> = repository.getStadiumList()
            call.enqueue(object : Callback<ListStadium> {
                override fun onResponse(call: Call<ListStadium>, response: Response<ListStadium>) {
                    if (response.isSuccessful) {
                        extractStadiumList(response.body()!!)
                    } else {
                        liveDataFlow.postValue(NetworkResult.Error(response.body().toString()))
                    }
                }

                override fun onFailure(call: Call<ListStadium>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

            })
        }
    }

    private fun extractStadiumList(body: ListStadium) {
        body.forEach {
            if (terrainIds.any { id -> id == it.id.toString() }) {
                _stadiums.postValue(it)
            }
        }
    }



}