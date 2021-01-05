package me.andreandyp.androidtechnicalchallenge.network.sepomex

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SettlementTypeNetwork(
    val key: String,
    val name: String
)