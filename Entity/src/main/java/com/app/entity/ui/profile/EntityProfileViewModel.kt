package com.app.entity.ui.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.entity.utils.NetworkResult
import com.app.networking.api.AuthRetrofitServiceInterface
import com.app.networking.model.user.UserAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class EntityProfileViewModel @Inject constructor(
    private val repository: AuthRetrofitServiceInterface,
    @ApplicationContext private val context: Context
) :
    ViewModel() {
    val liveUserAuthFlow: MutableLiveData<NetworkResult<UserAuth>> = MutableLiveData()

    private val _liveUserData = MutableLiveData<UserAuth>(UserAuth())
    val liveUser: LiveData<UserAuth> = _liveUserData


    fun getAuthUser() {
        val call: Call<UserAuth> = repository.getUserConnected()
        call.enqueue(object : Callback<UserAuth> {
            override fun onResponse(
                call: Call<UserAuth>,
                response: Response<UserAuth>
            ) {
                if (response.isSuccessful) {
                    Log.d("TAG", response.body().toString())
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
}