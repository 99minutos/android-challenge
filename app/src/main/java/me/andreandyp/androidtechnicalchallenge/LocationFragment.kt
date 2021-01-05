package me.andreandyp.androidtechnicalchallenge

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import me.andreandyp.androidtechnicalchallenge.databinding.LocationFragmentBinding

class LocationFragment : Fragment() {

    private lateinit var viewModel: LocationViewModel
    private lateinit var mapPolygon: SupportMapFragment
    private lateinit var binding: LocationFragmentBinding
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.location_fragment, container, false)
        mapPolygon = childFragmentManager.findFragmentById(R.id.map_polygon) as SupportMapFragment

        mapPolygon.getMapAsync { googleMap: GoogleMap ->
            map = googleMap
            val downtown = LatLng(19.432723850153973, -99.13313084558676)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(downtown, 14f))
            viewModel.onReadyMap()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        viewModel.geoPoints.observe(this) { points: List<LatLng> ->
            val polygonOptions = PolygonOptions().run {
                addAll(points)
                strokeColor(Color.BLACK)
                fillColor(R.color.black_light)
                geodesic(true)
            }
            map.addPolygon(polygonOptions)
        }
    }
}