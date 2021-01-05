package me.andreandyp.androidtechnicalchallenge.network.polygon

import retrofit2.http.GET
import retrofit2.http.Path

interface PolygonsService {
    @GET("/zip-codes/{zip}")
    suspend fun getPolygonVertices(@Path("zip") zip: String): PolygonNetwork
}