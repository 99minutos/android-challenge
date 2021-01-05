package me.andreandyp.androidtechnicalchallenge.network.polygon

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.JsonClass
import me.andreandyp.androidtechnicalchallenge.repository.models.PolygonCoordinates

@JsonClass(generateAdapter = true)
data class PolygonNetwork(
    val type: String,
    val geometry: GeometryNetwork,
) {
    fun asPolygonCoordinates(): PolygonCoordinates {
        val coordinates: List<LatLng> = this.geometry.coordinates[0].map {
            LatLng(it[1], it[0])
        }
        return PolygonCoordinates(coordinates)
    }
}