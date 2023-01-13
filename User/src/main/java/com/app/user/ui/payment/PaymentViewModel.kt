package com.app.user.ui.payment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.networking.model.entity.Stadium
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
class PaymentViewModel  @Inject constructor(
    private val repository: AuthRetrofitServiceRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    val liveDataFlow: MutableLiveData<NetworkResult<Stadium>> = MutableLiveData()
    private val _stadiums = MutableLiveData<Stadium>()
    val stadiumsData: LiveData<Stadium> = _stadiums

    suspend fun getStadiumById(idStadium: String) {
        liveDataFlow.postValue(NetworkResult.Loading())
        viewModelScope.launch {
            val call: Call<Stadium> = repository.getStadiumById(idStadium)
            call.enqueue(object : Callback<Stadium> {
                override fun onResponse(call: Call<Stadium>, response: Response<Stadium>) {
                    if (response.isSuccessful) {
                        _stadiums.postValue(response.body())
                        liveDataFlow.postValue(NetworkResult.Success(response.body()))
                    } else {
                        liveDataFlow.postValue(NetworkResult.Error(response.body().toString()))
                    }
                }

                override fun onFailure(call: Call<Stadium>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                    liveDataFlow.postValue(NetworkResult.Error("Error"))
                }

            })
        }
    }

}