package com.app.networking.model.reservation

data class Seance(
    val annee: Int,
    val disponibilite: Boolean,
    val heureDebut: String,
    val heureFin: String,
    val id: Int,
    val jour: Int,
    val mois: Int,
    val nbreParticipant: Int
)