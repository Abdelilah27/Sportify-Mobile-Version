package com.app.networking.model.user

import com.app.networking.model.app.AppRole

// User Connected
data class UserAuth(
    val age: String,
    val appRoles: List<AppRole>,
    val gendre: String,
    val id: Int,
    val username: String
)