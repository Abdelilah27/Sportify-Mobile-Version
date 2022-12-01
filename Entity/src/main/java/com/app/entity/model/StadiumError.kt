package com.app.entity.model

import com.app.entity.R

data class StadiumError(
    var nameError: Int = R.string.empty,
    var numberOfPlayerError: Int = R.string.empty,
    var priceError: Int = R.string.empty,
    var locationError: Int = R.string.empty,
    var disponibility_to: Int = R.string.empty,
    var disponibility_from: Int = R.string.empty,
    var imgFileName: Int = R.string.empty,
) {
}