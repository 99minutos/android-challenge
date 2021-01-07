package me.andreandyp.androidtechnicalchallenge.repository.models

import me.andreandyp.androidtechnicalchallenge.network.sepomex.ZipCodeDetailNetwork

/**
 * Domain data class which holds the zip code details obtained from the API.
 * @param locality locality of the zip code
 * @param federalEntity entity of the zip code
 * @param settlements list of all settlements of the zip code
 * @param municipality municipality of the zip code.
 */
data class ZipCodeSettlements(
    val locality: String,
    val federalEntity: String,
    val settlements: List<String>,
    val municipality: String
)

/**
 * Transforms the Sepomex API data to domain data.
 * @return a [ZipCodeSettlements] instance.
 */
fun ZipCodeDetailNetwork.asZipCodeSettlements(): ZipCodeSettlements {
    return ZipCodeSettlements(
        locality = this.locality,
        federalEntity = this.federalEntity.name,
        settlements = this.settlements.map { it.name },
        municipality = this.municipality.name
    )
}