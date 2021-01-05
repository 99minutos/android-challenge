package me.andreandyp.androidtechnicalchallenge.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.andreandyp.androidtechnicalchallenge.network.API
import me.andreandyp.androidtechnicalchallenge.repository.models.PolygonCoordinates

class AppRepository {
    suspend fun getPolygonCoordinates(id: String): PolygonCoordinates {
        return withContext(Dispatchers.IO) {
            val polygonCoordinatesNetwork = API.polygons.getPolygonVertices(id)
            return@withContext polygonCoordinatesNetwork.asPolygonCoordinates()
        }
    }
}