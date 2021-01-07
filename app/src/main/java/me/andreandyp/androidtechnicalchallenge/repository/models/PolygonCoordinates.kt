package me.andreandyp.androidtechnicalchallenge.repository.models

import com.google.android.gms.maps.model.LatLng
import me.andreandyp.androidtechnicalchallenge.network.polygon.PolygonNetwork

data class PolygonCoordinates(
    val coordinates: List<LatLng>
)

fun PolygonNetwork.asPolygonCoordinates(): PolygonCoordinates {
    val coordinates: List<LatLng> = this.geometry.coordinates[0].map {
        LatLng(it[1], it[0])
    }
    return PolygonCoordinates(coordinates)
}