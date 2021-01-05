package me.andreandyp.androidtechnicalchallenge.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import me.andreandyp.androidtechnicalchallenge.network.polygon.PolygonsService
import me.andreandyp.androidtechnicalchallenge.network.sepomex.SepomexService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val POLYGONS_URL = "https://poligonos-wje6f4jeia-uc.a.run.app"
const val SEPOMEX_URL = "https://sepomex-wje6f4jeia-uc.a.run.app"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofitPolygons = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(POLYGONS_URL)
    .build()

private val retrofitSepomex = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(SEPOMEX_URL)
    .build()

object API {
    val polygons: PolygonsService by lazy {
        retrofitPolygons.create(PolygonsService::class.java)
    }

    val sepomex: SepomexService by lazy {
        retrofitSepomex.create(SepomexService::class.java)
    }
}