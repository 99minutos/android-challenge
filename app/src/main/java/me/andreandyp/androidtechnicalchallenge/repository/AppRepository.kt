package me.andreandyp.androidtechnicalchallenge.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.andreandyp.androidtechnicalchallenge.network.API
import me.andreandyp.androidtechnicalchallenge.network.NetworkResponse
import me.andreandyp.androidtechnicalchallenge.repository.models.asPolygonCoordinates
import me.andreandyp.androidtechnicalchallenge.repository.models.asZipCodeSettlements
import retrofit2.HttpException
import java.io.IOException

class AppRepository {
    suspend fun getPolygonCoordinates(zip: String): NetworkResponse<Any?> {
        return withContext(Dispatchers.IO) {
            try {
                val polygonCoordinatesNetwork = API.polygons.getPolygonVertices(zip)
                val polygonCoordinates = polygonCoordinatesNetwork.asPolygonCoordinates()
                return@withContext NetworkResponse.Success(polygonCoordinates)
            } catch (e: HttpException) {
                return@withContext NetworkResponse.Error(e.code())
            } catch (e: IOException) {
                return@withContext NetworkResponse.NetworkError(e.message)
            }
        }
    }

    suspend fun getSettlementsOfZipCode(zip: String): NetworkResponse<Any?> {
        return withContext(Dispatchers.IO) {
            try {
                val zipCodeDetails = API.sepomex.getZipCodeDetail(zip)
                val settlements = zipCodeDetails.asZipCodeSettlements()
                return@withContext NetworkResponse.Success(settlements)
            } catch (e: HttpException) {
                return@withContext NetworkResponse.Error(e.code())
            } catch (e: IOException) {
                return@withContext NetworkResponse.NetworkError(e.message)
            }
        }
    }
}