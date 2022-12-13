package com.app.entity.ui.addStadium

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.entity.R
import com.app.entity.model.StadiumError
import com.app.entity.model.StadiumResponse
import com.app.entity.model.Terrain
import com.app.entity.repository.RetrofitServiceRepository
import com.app.entity.utils.ConstUtil.TIME24HOURS_PATTERN
import com.app.entity.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class AddStadiumViewModel @Inject constructor(
    private val repository: RetrofitServiceRepository,
    @ApplicationContext private val context: Context
) :
    ViewModel() {
    private val _liveStadiumData = MutableLiveData<Terrain>(Terrain())
    val liveStadium: LiveData<Terrain> = _liveStadiumData

    private val _liveStadiumError = MutableLiveData<StadiumError>(StadiumError())
    val liveErrorStadium: LiveData<StadiumError> = _liveStadiumError

    // Handle Error
    val liveAddStadiumFlow: MutableLiveData<NetworkResult<StadiumResponse>> = MutableLiveData()


    fun onRegistrationClicked(
        numberOfPlayer: String,
        price: String,
        pickedBitMap: Uri?,
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
            liveAddStadiumFlow.postValue(NetworkResult.Loading())
            val stadiumInfo =
                liveStadium.value!!.copy()
            val gson = Gson()
            val terrainJSON = gson.toJson(stadiumInfo)


            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = terrainJSON.toRequestBody(mediaType)

            viewModelScope.launch {
                val call: Call<StadiumResponse> = repository.saveStadium(requestBody)
                call.enqueue(object : Callback<StadiumResponse> {
                    override fun onResponse(
                        call: Call<StadiumResponse>,
                        response: Response<StadiumResponse>
                    ) {
                        if (response.isSuccessful) {
                            liveAddStadiumFlow.postValue(NetworkResult.Success(response.body()!!))
                        } else {
                            liveAddStadiumFlow.postValue(
                                NetworkResult.Error(
                                    response.body().toString()
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<StadiumResponse>, t: Throwable) {
                        liveAddStadiumFlow.postValue(NetworkResult.Error("Error"))
                    }

                })
            }


        }

        return isValid
    }

    private fun Time24HoursValidator(disponibility: String): Boolean =
        disponibility.matches(TIME24HOURS_PATTERN)

}