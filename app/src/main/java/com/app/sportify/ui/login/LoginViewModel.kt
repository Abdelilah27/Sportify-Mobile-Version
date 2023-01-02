package com.app.sportify.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.networking.model.app.UserLogin
import com.app.networking.model.app.response.TokenResponse
import com.app.networking.model.network.TokenManager
import com.app.networking.model.user.User
import com.app.networking.model.user.UserAuth
import com.app.networking.repository.AuthRetrofitServiceRepository
import com.app.networking.repository.RetrofitServiceRepository
import com.app.sportify.R
import com.app.sportify.model.UserError
import com.app.sportify.utils.NetworkResult
import com.app.sportify.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: RetrofitServiceRepository,
    private val repositoryAuth: AuthRetrofitServiceRepository,
    @ApplicationContext private val context: Context
) :
    ViewModel() {

    private val _liveUserData = MutableLiveData<User>(User())
    val liveUser: LiveData<User> = _liveUserData

    // Handle Errors
    private val _liveUserError = MutableLiveData<UserError>(UserError())
    val liveErrorUser: LiveData<UserError> = _liveUserError

    val liveUserFlow: MutableLiveData<NetworkResult<TokenResponse>> = MutableLiveData()
    val liveUserAuthFlow: MutableLiveData<NetworkResult<UserAuth>> = MutableLiveData()

    fun onLoginClicked(): Boolean {
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
        liveUserFlow.postValue(NetworkResult.Loading())
        val call: Call<TokenResponse> = repository.login(userLogin)
        call.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
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

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                liveUserFlow.postValue(NetworkResult.Error("Error"))
            }
        })
    }

    private fun getAuthUser() {
        liveUserAuthFlow.postValue(NetworkResult.Loading())
        val call: Call<UserAuth> = repositoryAuth.getUserConnected()
        call.enqueue(object : Callback<UserAuth> {
            override fun onResponse(
                call: Call<UserAuth>,
                response: Response<UserAuth>
            ) {
                if (response.isSuccessful) {
                    // Save User Data
                    val user = UserManager(context)
                    user.saveUserAuth(response.body()!!)
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