package me.andreandyp.androidtechnicalchallenge.repository.models

import com.google.android.gms.maps.model.LatLng
import me.andreandyp.androidtechnicalchallenge.network.polygon.PolygonNetwork

/**
 * Domain data class which holds the polygon coordinates obtained from the API.
 * @param coordinates List of coordinates as [LatLng] objects.
 */
data class PolygonCoordinates(
    val coordinates: List<LatLng>
)

/**
 * Transforms the Polygon API data to domain data.
 * @return a [PolygonCoordinates] instance.
 */
fun PolygonNetwork.asPolygonCoordinates(): PolygonCoordinates {
    val coordinates: List<LatLng> = this.geometry.coordinates[0].map {
        LatLng(it[1], it[0])
    }
    return PolygonCoordinates(coordinates)
}