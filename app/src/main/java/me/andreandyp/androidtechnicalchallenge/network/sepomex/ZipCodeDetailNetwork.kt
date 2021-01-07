package me.andreandyp.androidtechnicalchallenge.network.sepomex

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ZipCodeDetailNetwork(
    @Json(name = "zip_code")
    val zipCode: String,
    val locality: String,
    @Json(name = "federal_entity")
    val federalEntity: FederalEntityNetwork,
    val settlements: List<SettlementNetwork>,
    val municipality: MunicipalityNetwork
)