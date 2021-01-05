package me.andreandyp.androidtechnicalchallenge.network.sepomex

import retrofit2.http.GET
import retrofit2.http.Path

interface SepomexService {
    @GET("/api/zip-codes/{zip}")
    suspend fun getZipCodeDetail(@Path("zip") zip: String): ZipCodeDetailNetwork
}