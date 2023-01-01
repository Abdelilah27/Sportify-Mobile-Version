package com.app.networking.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Stadium(
    @SerializedName("description")
    var description: String? = "",
    @SerializedName("disponibility_from")
    var disponibility_from: String? = "",
    @SerializedName("disponibility_to")
    var disponibility_to: String? = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("imgFileName")
    var imgFileName: String? = "",
    @SerializedName("location")
    var location: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("numberOfPlayer")
    var numberOfPlayer: Int = 0,
    @SerializedName("price")
    var price: Int = 0,
) : Serializable {

}