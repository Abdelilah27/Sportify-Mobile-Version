package com.app.entity.ui.addStadium

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.entity.R
import com.app.entity.model.Stadium
import com.app.entity.model.StadiumError
import com.app.entity.model.StadiumResponse
import com.app.entity.repository.RetrofitServiceRepository
import com.app.entity.utils.ConstUtil.TIME24HOURS_PATTERN
import com.app.entity.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class AddStadiumViewModel @Inject constructor(private val repository: RetrofitServiceRepository) :
    ViewModel() {
    private val _liveStadiumData = MutableLiveData<Stadium>(Stadium())
    val liveStadium: LiveData<Stadium> = _liveStadiumData

    private val _liveStadiumError = MutableLiveData<StadiumError>(StadiumError())
    val liveErrorStadium: LiveData<StadiumError> = _liveStadiumError

    // Handle Error
    val liveAddStadiumFlow: MutableLiveData<NetworkResult<StadiumResponse>> = MutableLiveData()

    fun onRegistrationClicked(
        numberOfPlayer: String,
        price: String,
        pickedBitMap: Bitmap?,
    ): Boolean {
        // Handle Errors
        var isValid = true
        if (liveStadium.value?.name.isNullOrEmpty()) {
            _liveStadiumError.postValue(StadiumError(nameError = R.string.name_error))
            isValid = false
        } else if (numberOfPlayer.isEmpty()) {
            _liveStadiumError.postValue(StadiumError(numberOfPlayerError = R.string.number_error))
            isValid = false
        } else if (price.isEmpty()) {
            _liveStadiumError.postValue(StadiumError(priceError = R.string.price_error))
            isValid = false
        } else if (liveStadium.value?.location.isNullOrEmpty()) {
            _liveStadiumError.postValue(StadiumError(locationError = R.string.location_error))
            isValid = false
        } else if (!Time24HoursValidator(
                liveStadium.value?.disponibility_from!!,
            )
        ) {
            _liveStadiumError.postValue(StadiumError(disponibility_from = R.string.disponibility_error))
            isValid = false
        } else if (!Time24HoursValidator(
                liveStadium.value?.disponibility_to!!
            )
        ) {
            _liveStadiumError.postValue(
                StadiumError(
                    disponibility_to = R.string
                        .disponibility_error
                )
            )
            isValid = false
        }
        if (isValid) {
            viewModelScope.launch {
                liveAddStadiumFlow.postValue(NetworkResult.Loading())
                val stadiumInfo =
                    liveStadium.value!!.copy()
                try {
                    val response = repository.saveStadium(stadiumInfo).awaitResponse()
                    if (response.isSuccessful) {
                        liveAddStadiumFlow.postValue(NetworkResult.Success(response.body()!!))
                    } else {
                        liveAddStadiumFlow.postValue(
                            NetworkResult.Error(
                                response.body().toString()
                            )
                        )
                    }
                } catch (ex: Exception) {
                    liveAddStadiumFlow.postValue(NetworkResult.Error(ex.message.toString()))
                    Log.d("RARA2é", liveAddStadiumFlow.value?.message.toString())
                }
            }
        }

        return isValid
    }

    private fun Time24HoursValidator(disponibility: String): Boolean =
        disponibility.matches(TIME24HOURS_PATTERN)


}