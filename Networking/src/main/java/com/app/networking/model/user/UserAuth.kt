package com.app.networking.model.user

// User Connected
data class UserAuth(
    val age: String = "",
    val appRoles: List<AppRole> = listOf(),
    val gendre: String = "",
    val id: Int = 0,
    val username: String = ""
)