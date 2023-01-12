package com.app.user.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.networking.api.AuthRetrofitServiceInterface
import com.app.networking.model.network.TokenManager
import com.app.networking.model.user.UserAuth
import com.app.networking.repository.AuthRetrofitServiceRepository
import com.app.user.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AuthRetrofitServiceRepository,
) :
    ViewModel() {
    val liveUserAuthFlow: MutableLiveData<NetworkResult<UserAuth>> = MutableLiveData()
    val liveUserLogOutFlow: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()

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

    fun logOut() {
        liveUserLogOutFlow.postValue(NetworkResult.Loading())
        val tokenManager = TokenManager(context)
        try {
            tokenManager.clearToken()
            liveUserLogOutFlow.postValue(NetworkResult.Success(true))
        } catch (e: Exception) {
            liveUserLogOutFlow.postValue(NetworkResult.Error(e.toString()))
        }
    }

}