package com.app.networking.model.reservation

data class ReservationByUserResponseItem(
    val id: Int,
    val joueurs: List<String>,
    val seance: Any,
    val seanceId: Int,
    val terrain: Any,
    val terrainId: Int,
    val user: Any,
    val userId: Int
)