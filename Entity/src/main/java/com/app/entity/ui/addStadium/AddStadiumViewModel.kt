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
import com.app.entity.repository.RetrofitServiceRepository
import com.app.entity.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddStadiumViewModel @Inject constructor(private val repository: RetrofitServiceRepository) :
    ViewModel() {
    private val _liveStadiumData = MutableLiveData<Stadium>(Stadium())
    val liveStadium: LiveData<Stadium> = _liveStadiumData

    private val _liveStadiumError = MutableLiveData<StadiumError>(StadiumError())
    val liveErrorStadium: LiveData<StadiumError> = _liveStadiumError

    // AddStadium Statues
    val liveAddStadiumFlow = MutableLiveData<Resource.Status>(Resource.Status.NONE)

    fun onRegistrationClicked(
        disponibility_from: String,
        disponibility_to: String,
    ): Boolean {
        // Handle Errors
        var isValid = true
        if (liveStadium.value?.name.isNullOrEmpty()) {
            _liveStadiumError.postValue(StadiumError(nameError = R.string.name_error))
            isValid = false
        } else if (liveStadium.value?.numberOfPlayer.isNullOrEmpty()) {
            _liveStadiumError.postValue(StadiumError(numberOfPlayerError = R.string.number_error))
            isValid = false
        } else if (liveStadium.value?.price.isNullOrEmpty()) {
            _liveStadiumError.postValue(StadiumError(priceError = R.string.price_error))
            isValid = false
        } else if (liveStadium.value?.location.isNullOrEmpty()) {
            _liveStadiumError.postValue(StadiumError(locationError = R.string.location_error))
            isValid = false
        } else if (!validDate(disponibility_from, disponibility_to)) {
            _liveStadiumError.postValue(StadiumError(disponibility_to = R.string.disponibility_error))
            isValid = false
        }
        if (isValid) {
            viewModelScope.launch {
                liveAddStadiumFlow.postValue(Resource.Status.LOADING)
//                liveAddStadiumFlow.postValue(Resource.Status.SUCCESS)
//                liveAddStadiumFlow.postValue(Resource.Status.SUCCESS)
            }
        }

        return isValid
    }

    private fun validDate(disponibilityFrom: String, disponibilityTo: String): Boolean {
        if (disponibilityFrom.isNotEmpty() && disponibilityTo.isNotEmpty()) {
            return disponibilityFrom.toInt() < disponibilityTo.toInt()
        }
        return true
    }
}