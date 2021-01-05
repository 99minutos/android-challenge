package me.andreandyp.androidtechnicalchallenge.network.polygon

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeometryNetwork(
    val type: String,
    val coordinates: List<List<List<Double>>>
)