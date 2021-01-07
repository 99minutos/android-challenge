package me.andreandyp.androidtechnicalchallenge.network.polygon

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PolygonNetwork(
    val type: String,
    val geometry: GeometryNetwork,
)