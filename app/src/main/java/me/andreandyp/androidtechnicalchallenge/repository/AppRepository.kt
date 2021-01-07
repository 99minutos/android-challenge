package me.andreandyp.androidtechnicalchallenge.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.andreandyp.androidtechnicalchallenge.network.API
import me.andreandyp.androidtechnicalchallenge.network.NetworkResponse
import me.andreandyp.androidtechnicalchallenge.repository.models.asPolygonCoordinates
import me.andreandyp.androidtechnicalchallenge.repository.models.asZipCodeSettlements
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository to access to the data from the API.
 */
class AppRepository {

    /**
     * Calls the polygon API with the required zip code.
     * @param zipCode the zip code required.
     * @return a [NetworkResponse] wrapper with the results transformed to domain objects
     * or the status code when the request failed.
     */
    suspend fun getPolygonCoordinates(zipCode: String): NetworkResponse<Any?> {
        return withContext(Dispatchers.IO) {
            try {
                val polygonCoordinatesNetwork = API.polygons.getPolygonVertices(zipCode)
                val polygonCoordinates = polygonCoordinatesNetwork.asPolygonCoordinates()
                return@withContext NetworkResponse.Success(polygonCoordinates)
            } catch (e: HttpException) {
                return@withContext NetworkResponse.Error(e.code())
            } catch (e: IOException) {
                return@withContext NetworkResponse.NetworkError(e.message)
            }
        }
    }

    /**
     * Calls the Sepomex API with the required zip code.
     * @param zipCode the zip code required.
     * @return a [NetworkResponse] wrapper with the results transformed to domain objects
     * or the status code when the request failed.
     */
    suspend fun getDetailsOfZipCode(zipCode: String): NetworkResponse<Any?> {
        return withContext(Dispatchers.IO) {
            try {
                val zipCodeDetails = API.sepomex.getZipCodeDetail(zipCode)
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