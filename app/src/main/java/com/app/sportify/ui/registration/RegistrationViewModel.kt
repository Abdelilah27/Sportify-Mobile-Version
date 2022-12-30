package com.app.sportify.ui.registration

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sportify.R
import com.app.sportify.model.User
import com.app.sportify.model.UserError
import com.app.sportify.model.UserResponse
import com.app.sportify.repository.AppRetrofitServiceRepository
import com.app.sportify.utils.FunUtil.toSHA256Hash
import com.app.sportify.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: AppRetrofitServiceRepository,
    @ApplicationContext private val context: Context
) :
    ViewModel() {
    private val _liveUserData = MutableLiveData<User>(User())
    val liveUser: LiveData<User> = _liveUserData

    // Handle Errors
    private val _liveUserError = MutableLiveData<UserError>(UserError())
    val liveErrorUser: LiveData<UserError> = _liveUserError

    // Handle progress bar state
    val liveUserFlow: MutableLiveData<NetworkResult<UserResponse>> = MutableLiveData()


    fun onRegistrationClicked(
        confirmedPassword: String,
        appRole: String,
        age: String,
        gendre: String
    ): Boolean {
        // Handle Errors
        var isValid = true
        if (liveUser.value?.username.isNullOrEmpty()) {
            _liveUserError.postValue(UserError(usernameError = R.string.username_error))
            isValid = false
        }
        if (isValid && appRole.isEmpty()) {
            _liveUserError.postValue(UserError(appRoleError = R.string.app_role_error))
            isValid = false
        }

        if (isValid && liveUser.value?.password.isNullOrEmpty()) {
            _liveUserError.postValue(UserError(passwordError = R.string.password_error))
            isValid = false
        }
        if (isValid && !isPasswordLengthGreaterThan5(liveUser.value?.password!!)) {
            _liveUserError.postValue(UserError(passwordError = R.string.password_error_regex))
            isValid = false
        }
        if (isValid && confirmedPassword.isEmpty()) {
            _liveUserError.postValue(
                UserError(
                    confirmedPasswordError = R.string.verification_password_error
                )
            )
            isValid = false
        }
        if (isValid && confirmedPassword != liveUser.value?.password) {
            _liveUserError.postValue(
                UserError(
                    passwordError = R.string
                        .verification_password_not_match_error
                )
            )
            isValid = false
        }
        if (isValid) {
            viewModelScope.launch {
                val userInfo =
                    liveUser.value!!.copy(
                        age = age,
                        gendre = gendre,
                        password = liveUser.value!!.password.toSHA256Hash()
                    )
                liveUserFlow.postValue(NetworkResult.Loading())
                val call: Call<UserResponse> = repository.createUser(userInfo)
                call.enqueue(object : Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
                    ) {
                        if (response.isSuccessful) {
                            liveUserFlow.postValue(NetworkResult.Success(response.body()!!))
                        } else {
                            liveUserFlow.postValue(
                                NetworkResult.Error(
                                    response.body().toString()
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        liveUserFlow.postValue(NetworkResult.Error("Error"))
                    }

                })
            }


        }
        return isValid
    }

    private fun isPasswordLengthGreaterThan5(password: String): Boolean {
        return password.length > 5
    }


}