package com.app.entity.ui.stadiums

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.entity.model.ListStadium
import com.app.entity.repository.RetrofitServiceRepository
import com.app.entity.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class StadiumsViewModel @Inject constructor(
    private val repository: RetrofitServiceRepository,
    @ApplicationContext private val context: Context
) :
    ViewModel() {
    // Handle error
    val liveDataFlow: MutableLiveData<NetworkResult<ListStadium>> = MutableLiveData()

    private val _stadiums = MutableLiveData<Response<ListStadium>>()
    val stadiums: LiveData<Response<ListStadium>> = _stadiums


    // Handle Error
    val liveStadiumsFlow: MutableLiveData<NetworkResult<ResponseBody>> = MutableLiveData()

    suspend fun getStadiumList() {
        liveDataFlow.postValue(NetworkResult.Loading())
        try {
            val stadiums = repository.getStadiumList()
            _stadiums.postValue(stadiums)
            liveDataFlow.postValue(NetworkResult.Success(stadiums.body()!!))
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> {
                    liveDataFlow.postValue(
                        NetworkResult.Error(
                            "Network " +
                                    "Failure " + ex.localizedMessage
                        )
                    )
                }
                else -> {
                    liveDataFlow.postValue(NetworkResult.Error(ex.toString()))
                }
            }
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