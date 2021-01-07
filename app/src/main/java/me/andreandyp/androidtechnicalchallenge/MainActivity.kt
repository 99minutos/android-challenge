package me.andreandyp.androidtechnicalchallenge

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: LocationViewModel
    private lateinit var mapPolygon: SupportMapFragment
    private var currentPolygon: Polygon? = null
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = ""
        mapPolygon = supportFragmentManager.findFragmentById(R.id.map_polygon) as SupportMapFragment

        mapPolygon.getMapAsync { googleMap: GoogleMap ->
            map = googleMap
            viewModel.onReadyMap()
        }

        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        viewModel.geoPoints.observe(this) { updatePolygon(it) }
        viewModel.centerPoint.observe(this) { point: LatLng ->
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 14f))
        }
    }

    /**
     * Updates the location of the current polygon on the map.
     * @param points the polygon vertices obtained from the repository.
     */
    private fun updatePolygon(points: List<LatLng>) {
        currentPolygon?.remove()
        val polygonOptions = PolygonOptions().run {
            addAll(points)
            strokeColor(Color.BLACK)
            fillColor(R.color.black_light)
            geodesic(true)
        }
        currentPolygon = map?.addPolygon(polygonOptions)
    }
}