package me.andreandyp.androidtechnicalchallenge.network.sepomex

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import me.andreandyp.androidtechnicalchallenge.repository.models.ZipCodeSettlements

@JsonClass(generateAdapter = true)
data class ZipCodeDetailNetwork(
    @Json(name = "zip_code")
    val zipCode: String,
    val locality: String,
    @Json(name = "federal_entity")
    val federalEntity: FederalEntityNetwork,
    val settlements: List<SettlementNetwork>,
    val municipality: MunicipalityNetwork
) {
    fun asZipCodeSettlements(): ZipCodeSettlements {
        return ZipCodeSettlements(
            locality = this.locality,
            federalEntity = this.federalEntity.name,
            settlements = this.settlements.map { it.name },
            municipality = this.municipality.name
        )
    }
}