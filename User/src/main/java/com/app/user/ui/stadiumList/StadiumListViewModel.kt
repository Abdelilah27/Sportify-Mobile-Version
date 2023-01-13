package com.app.user.ui.stadiumList

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


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
                        response.body()?.let {
                            extractListByName(entityName, it)
                        }
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

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat", "NewApi")
    fun getAvailableStadiums(stadiums: List<Stadium>, day: Int): List<Stadium> {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        return stadiums.filter { stadium ->
            try {
                val disponibilityFrom = formatter.parse(stadium.disponibility_from)
                val disponibilityTo = formatter.parse(stadium.disponibility_to)
                val calFrom = Calendar.getInstance().apply { time = disponibilityFrom }
                val calTo = Calendar.getInstance().apply { time = disponibilityTo }
                (calFrom.get(Calendar.DAY_OF_MONTH) == day) && (calTo.get(Calendar.DAY_OF_MONTH) == day)
            } catch (e: java.text.ParseException) {
                // handle the exception here
                println("Error while parsing date: ${e.message}")
                // or you can return false to filter out this element
                false
            }
        }
    }






    private fun extractListByName(entityName: String, body: ListStadium) {
        val stadiumsList = ArrayList<Stadium>()
        body.forEach {
            if (it.entity == entityName) {
                stadiumsList.add(it)
            }
        }
        _stadiums.postValue(stadiumsList)
    }
}