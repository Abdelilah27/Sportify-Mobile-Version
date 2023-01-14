package com.app.networking.model.reservation.orderNomCompletResponse

data class OrderNonCompletResponseItem(
    val description: String? = null,
    val disponibility_from: String? = null,
    val disponibility_to: String? = null,
    val entity: String? = null,
    val id: Int = 0,
    val imgFileName: String? = null,
    val location: String? = null,
    val name: String? = null,
    val price: Int = 0,
    val seances: List<Seance> = listOf()
)