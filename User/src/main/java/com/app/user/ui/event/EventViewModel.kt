package com.app.user.ui.event

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.networking.model.entity.ListStadium
import com.app.networking.model.entity.Stadium
import com.app.networking.model.reservation.ReservationResponse
import com.app.networking.repository.AuthRetrofitServiceRepository
import com.app.user.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: AuthRetrofitServiceRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    val liveDataStadiumFlow: MutableLiveData<NetworkResult<ReservationResponse>> = MutableLiveData()
    private val _reservationResponse = MutableLiveData<ReservationResponse>()
    val reservationResponse: LiveData<ReservationResponse> = _reservationResponse

    val liveDataBookFlow: MutableLiveData<NetworkResult<ReservationResponse>> = MutableLiveData()

    suspend fun reserveSeance(idStadium: String, idSeance: String) {
        liveDataStadiumFlow.postValue(NetworkResult.Loading())
        viewModelScope.launch {
            val call: Call<ReservationResponse> = repository.reserveSeance(idStadium, idSeance)
            call.enqueue(object : Callback<ReservationResponse> {
                override fun onResponse(call: Call<ReservationResponse>, response: Response<ReservationResponse>) {
                    Log.d("response", response.body().toString())
                    Log.d("response", response.code().toString())
                    Log.d("response", response.message().toString())
                    if (response.isSuccessful) {
                        _reservationResponse.postValue(response.body())
                        liveDataStadiumFlow.postValue(NetworkResult.Success(response.body()))
                    } else {
                        liveDataStadiumFlow.postValue(NetworkResult.Error(response.body().toString()))
                    }
                }

                override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                    liveDataStadiumFlow.postValue(NetworkResult.Error("Error"))
                }

            })
        }
    }

    suspend fun bookSeance(idStadium: String, idSeance: String) {
        liveDataStadiumFlow.postValue(NetworkResult.Loading())
        viewModelScope.launch {
            val call: Call<ReservationResponse> = repository.reserveSeance(idStadium, idSeance)
            call.enqueue(object : Callback<ReservationResponse> {
                override fun onResponse(call: Call<ReservationResponse>, response: Response<ReservationResponse>) {
                    Log.d("response", response.body().toString())
                    Log.d("response", response.code().toString())
                    Log.d("response", response.message().toString())
                    if (response.isSuccessful) {
                        liveDataBookFlow.postValue(NetworkResult.Success(response.body()))
                    } else {
                        liveDataBookFlow.postValue(NetworkResult.Error(response.body().toString()))
                    }
                }

                override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                    liveDataBookFlow.postValue(NetworkResult.Error("Error"))
                }

            })
        }
    }
}