package com.app.networking.model.app.response

data class UserLoginResponse(
    val access_token: String,
    val refresh_token: String
)