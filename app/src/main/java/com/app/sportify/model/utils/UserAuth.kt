package com.app.sportify.model.utils

// User Connected
data class UserAuth(
    val age: String,
    val appRoles: List<AppRole>,
    val gendre: String,
    val id: Int,
    val username: String
)