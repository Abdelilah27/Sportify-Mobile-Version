package com.app.networking.model.app.response

data class UserResponse(
    val age: String,
    val appRoles: List<String>,
    val gendre: String,
    val id: Int,
    val username: String
)