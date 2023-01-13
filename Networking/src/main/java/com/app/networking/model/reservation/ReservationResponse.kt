package com.app.networking.model.reservation

data class ReservationResponse(
    val id: Int,
    val joueurs: List<String>,
    val seance: Seance,
    val seanceId: Int,
    val terrain: Terrain,
    val terrainId: Int,
    val user: User,
    val userId: Int
)