package me.andreandyp.androidtechnicalchallenge.network.sepomex

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FederalEntityNetwork(
    val key: String,
    val name: String,
    val code: String
)