package me.andreandyp.androidtechnicalchallenge.network.sepomex

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SettlementNetwork(
    val key: String,
    val name: String,
    @Json(name = "zone_type")
    val zoneType: String,
    @Json(name = "settlement_type")
    val settlementType: SettlementTypeNetwork,
)