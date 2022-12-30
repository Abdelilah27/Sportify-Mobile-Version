package com.app.sportify.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sportify.R
import com.app.sportify.model.TokenManager
import com.app.sportify.model.User
import com.app.sportify.model.UserError
import com.app.sportify.model.utils.UserAuth
import com.app.sportify.model.utils.UserLogin
import com.app.sportify.model.utils.UserLoginResponse
import com.app.sportify.repository.AppRetrofitAuthServiceRepository
import com.app.sportify.repository.AppRetrofitServiceRepository
import com.app.sportify.utils.NetworkResult
import com.app.sportify.utils.writeString
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AppRetrofitServiceRepository,
    private val repositoryAuth: AppRetrofitAuthServiceRepository,
    @ApplicationContext private val context: Context
) :
    ViewModel() {
//    val user = requireContext().readString("user_auth").asLiveData()

    private val _liveUserData = MutableLiveData<User>(User())
    val liveUser: LiveData<User> = _liveUserData

    // Handle Errors
    private val _liveUserError = MutableLiveData<UserError>(UserError())
    val liveErrorUser: LiveData<UserError> = _liveUserError

    val liveUserFlow: MutableLiveData<NetworkResult<UserLoginResponse>> = MutableLiveData()
    val liveUserAuthFlow: MutableLiveData<NetworkResult<UserAuth>> = MutableLiveData()

    fun onRegistrationClicked(): Boolean {
        // Handle Errors
        var isValid = true
        if (liveUser.value?.username.isNullOrEmpty()) {
            _liveUserError.postValue(UserError(usernameError = R.string.username_error))
            isValid = false
        }

        if (isValid && liveUser.value?.password.isNullOrEmpty()) {
            _liveUserError.postValue(UserError(passwordError = R.string.password_error))
            isValid = false
        }
        if (isValid) {
            val userLogin = UserLogin(
                username = liveUser.value!!.username, password = liveUser
                    .value!!.password
            )
            login(userLogin)
        }
        return isValid
    }

    private fun login(userLogin: UserLogin) {
        val call: Call<UserLoginResponse> = repository.login(userLogin)
        call.enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
                if (response.isSuccessful) {
                    // Store Token
                    val tokenManager = TokenManager(context)
                    tokenManager.saveToken(response.body()!!)

                    liveUserFlow.postValue(NetworkResult.Success(response.body()))
                    runBlocking { getAuthUser() }
                } else {
                    liveUserFlow.postValue(
                        NetworkResult.Error(
                            response.body().toString()
                        )
                    )
                }

            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                liveUserFlow.postValue(NetworkResult.Error("Error"))
            }
        })
    }

    private fun getAuthUser() {
        val call: Call<UserAuth> = repositoryAuth.getUserConnected()
        call.enqueue(object : Callback<UserAuth> {
            override fun onResponse(
                call: Call<UserAuth>,
                response: Response<UserAuth>
            ) {
                if (response.isSuccessful) {
                    liveUserAuthFlow.postValue(NetworkResult.Success(response.body()))
                    storeUser(response)
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

    private fun storeUser(response: Response<UserAuth>) {
        viewModelScope.launch {
            val gson = Gson()
            val json = gson.toJson(response.body())
            context.writeString("user_auth", json)
        }
    }
}