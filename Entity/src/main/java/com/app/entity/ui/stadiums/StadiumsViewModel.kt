package com.app.entity.ui.stadiums

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.entity.utils.NetworkResult
import com.app.networking.api.AuthRetrofitServiceInterface
import com.app.networking.model.entity.ListStadium
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class StadiumsViewModel @Inject constructor(
    private val repository: AuthRetrofitServiceInterface,
    @ApplicationContext private val context: Context
) :
    ViewModel() {
    // Handle error
    val liveDataFlow: MutableLiveData<NetworkResult<ListStadium>> = MutableLiveData()

    private val _stadiums = MutableLiveData<Response<ListStadium>>()
    val stadiums: LiveData<Response<ListStadium>> = _stadiums


    // Handle Error
    val liveStadiumsFlow: MutableLiveData<NetworkResult<ResponseBody>> = MutableLiveData()

    fun getStadiumList() {
        liveDataFlow.postValue(NetworkResult.Loading())
        viewModelScope.launch {
            val call: Call<ListStadium> = repository.getStadiumList()
            Log.d("TAG0", call.request().url.toString())
            call.enqueue(object : Callback<ListStadium> {
                override fun onResponse(call: Call<ListStadium>, response: Response<ListStadium>) {
                    if (response.isSuccessful) {
                        _stadiums.postValue(response)
                        liveDataFlow.postValue(NetworkResult.Success(response.body()))
                    } else {
                        Log.d("else", response.code().toString())
                        Log.d("else", response.body().toString())
                        liveDataFlow.postValue(NetworkResult.Error(response.body().toString()))
                    }
                }

                override fun onFailure(call: Call<ListStadium>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                    liveDataFlow.postValue(NetworkResult.Error("Error"))
                }

            })
        }
    }

    fun deleteStadium(id: String) {
        liveStadiumsFlow.postValue(NetworkResult.Loading())
        viewModelScope.launch {
            val call: Call<ResponseBody> = repository.deleteStadium(id)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        liveStadiumsFlow.postValue(NetworkResult.Success(response.body()))
                    } else {
                        liveStadiumsFlow.postValue(
                            NetworkResult.Error("Error")
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    liveStadiumsFlow.postValue(NetworkResult.Error("Error"))
                }

            })
        }
    }

}