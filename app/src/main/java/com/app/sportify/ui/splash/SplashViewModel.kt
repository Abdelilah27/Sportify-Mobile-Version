package com.app.sportify.ui.splash

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.networking.model.app.response.TokenResponse
import com.app.networking.model.network.TokenManager
import com.app.networking.repository.RetrofitServiceRepository
import com.app.sportify.utils.ConstUtil.ROLES
import com.app.sportify.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: RetrofitServiceRepository,
) :
    ViewModel() {
    // To Notify view
    private val _isLogged = MutableLiveData<String>()
    val isLogged: LiveData<String> = _isLogged

    fun isLogged() {
        val tokenManager = TokenManager(context)
        val accessToken = tokenManager.getAccessToken()
        val refreshToken = tokenManager.getRefreshToken()

        if (accessToken.isNullOrEmpty()) {
            _isLogged.postValue("noLogged")
        } else {
            if (checkRefreshToken(refreshToken.toString())) {
                _isLogged.postValue("noLogged")
            } else {
                val user = UserManager(context = context)
                val getUser = user.getUserAuth()
                if (getUser.appRoles.firstOrNull()?.toString() == ROLES[1]) {
                    _isLogged.postValue(ROLES[1])

                } else {
                    _isLogged.postValue(ROLES[0])

                }
            }
        }
    }

    private fun checkRefreshToken(refreshToken: String): Boolean {
        var success = false
        val call = repository.refreshToken("Bearer $refreshToken")
        call.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                success = response.isSuccessful
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                success = false
            }
        })
        return success
    }


}