package com.app.user.ui.stadiumList

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.networking.model.entity.ListStadium
import com.app.networking.model.entity.Stadium
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
class StadiumListViewModel @Inject constructor(
    private val repository: AuthRetrofitServiceRepository,
    @ApplicationContext private val context: Context,
    ) : ViewModel() {
    // Handle error
    val liveDataFlow: MutableLiveData<NetworkResult<ListStadium>> = MutableLiveData()
    private val _stadiums = MutableLiveData<List<Stadium>>()
    val stadiums: LiveData<List<Stadium>> = _stadiums

    suspend fun getStadiumList(entityName: String) {
        liveDataFlow.postValue(NetworkResult.Loading())
        viewModelScope.launch {
            val call: Call<ListStadium> = repository.getStadiumList()
            call.enqueue(object : Callback<ListStadium> {
                override fun onResponse(call: Call<ListStadium>, response: Response<ListStadium>) {
                    if (response.isSuccessful) {
                        response.body()?.let { extractListByName(entityName, it) }
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
    }

    private fun extractListByName(entityName: String, body: ListStadium) {
        val stadiumsList = ArrayList<Stadium>()
        body.forEach {
            if(it.entity == entityName){
                stadiumsList.add(it)
            }
        }
        _stadiums.postValue(stadiumsList)
    }
}