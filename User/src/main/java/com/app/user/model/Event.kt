package com.app.user.model

data class Event(
    var id: Int = 0,
    val price: String? = "",
    val numberOfPlayer: String? = "",
    val name: String? = "",
    var imgFileName: String? = "",
    var date: String? = "",
    var location: String? = ""
)