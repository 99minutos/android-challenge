package me.andreandyp.androidtechnicalchallenge.repository.models

import com.google.android.gms.maps.model.LatLng

data class PolygonCoordinates(
    val coordinates: List<LatLng>
)