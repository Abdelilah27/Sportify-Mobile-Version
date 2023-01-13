package com.app.networking.model.reservation

data class Terrain(
    val description: Any,
    val disponibility_from: String,
    val disponibility_to: String,
    val entity: String,
    val id: Int,
    val imgFileName: Any,
    val location: String,
    val name: String,
    val price: Int,
    val seances: List<Seance>
)