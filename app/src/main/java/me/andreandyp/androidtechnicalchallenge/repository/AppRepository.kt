package me.andreandyp.androidtechnicalchallenge.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.andreandyp.androidtechnicalchallenge.network.API
import me.andreandyp.androidtechnicalchallenge.repository.models.PolygonCoordinates
import me.andreandyp.androidtechnicalchallenge.repository.models.ZipCodeSettlements

class AppRepository {
    suspend fun getPolygonCoordinates(zip: String): PolygonCoordinates {
        return withContext(Dispatchers.IO) {
            val polygonCoordinatesNetwork = API.polygons.getPolygonVertices(zip)
            return@withContext polygonCoordinatesNetwork.asPolygonCoordinates()
        }
    }

    suspend fun getSettlementsOfZipCode(zip: String): ZipCodeSettlements {
        return withContext(Dispatchers.IO) {
            val zipCodeDetails = API.sepomex.getZipCodeDetail(zip)
            return@withContext zipCodeDetails.asZipCodeSettlements()
        }
    }
}