package com.app.networking.model.app.response

data class TokenResponse(
    val access_token: String,
    val refresh_token: String
)