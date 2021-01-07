package me.andreandyp.androidtechnicalchallenge.repository.models

import me.andreandyp.androidtechnicalchallenge.network.sepomex.ZipCodeDetailNetwork

data class ZipCodeSettlements(
    val locality: String,
    val federalEntity: String,
    val settlements: List<String>,
    val municipality: String
)

fun ZipCodeDetailNetwork.asZipCodeSettlements(): ZipCodeSettlements {
    return ZipCodeSettlements(
        locality = this.locality,
        federalEntity = this.federalEntity.name,
        settlements = this.settlements.map { it.name },
        municipality = this.municipality.name
    )
}