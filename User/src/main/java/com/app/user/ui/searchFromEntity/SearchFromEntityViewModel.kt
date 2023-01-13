package com.app.user.ui.searchFromEntity

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.networking.model.entity.ListStadium
import com.app.networking.model.user.Seances
import com.app.networking.model.user.SeancesItem
import com.app.networking.repository.AuthRetrofitServiceRepository
import com.app.user.model.Entity
import com.app.user.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchFromEntityViewModel @Inject constructor(
    private val repository: AuthRetrofitServiceRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    // Progress Bar
    val liveDataFlow: MutableLiveData<NetworkResult<Seances>> = MutableLiveData()

    private val _seances = MutableLiveData<List<SeancesItem>>()
    val seances: LiveData<List<SeancesItem>> = _seances

    suspend fun getSeancesList(id: String){
        liveDataFlow.postValue(NetworkResult.Loading())
        viewModelScope.launch {
            val call: Call<Seances> = repository.getSeanceByStadium(id)
            call.enqueue(object : Callback<Seances> {
                override fun onResponse(call: Call<Seances>, response: Response<Seances>) {
                    if (response.isSuccessful) {
                        _seances.postValue(response.body())
                        liveDataFlow.postValue(NetworkResult.Success(response.body()))
                    }else{
                        liveDataFlow.postValue(NetworkResult.Error(response.body().toString()))
                    }
                }

                override fun onFailure(call: Call<Seances>, t: Throwable) {
                    Log.d("Error",t.message.toString())
                    liveDataFlow.postValue(NetworkResult.Error("Error"))
                }

            })
        }
    }


}