package com.app.networking.model.reservation

data class User(
    val appRoles: List<AppRole>,
    val id: Int,
    val username: String
)