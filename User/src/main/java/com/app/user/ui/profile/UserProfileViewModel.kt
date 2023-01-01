package com.app.user.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.networking.api.AuthRetrofitServiceInterface
import com.app.networking.model.user.UserAuth
import com.app.user.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val repository: AuthRetrofitServiceInterface,
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