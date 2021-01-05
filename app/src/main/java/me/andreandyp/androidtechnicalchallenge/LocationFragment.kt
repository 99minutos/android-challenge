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
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import me.andreandyp.androidtechnicalchallenge.databinding.LocationFragmentBinding

class LocationFragment : Fragment() {

    private lateinit var viewModel: LocationViewModel
    private lateinit var mapPolygon: SupportMapFragment
    private lateinit var binding: LocationFragmentBinding
    private lateinit var map: GoogleMap
    private var currentPolygon: Polygon? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.location_fragment, container, false)
        binding.lifecycleOwner = this
        mapPolygon = childFragmentManager.findFragmentById(R.id.map_polygon) as SupportMapFragment

        mapPolygon.getMapAsync { googleMap: GoogleMap ->
            map = googleMap
            viewModel.onReadyMap()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        binding.vm = viewModel

        viewModel.geoPoints.observe(viewLifecycleOwner) { updatePolygon(it) }

        viewModel.centerPoint.observe(viewLifecycleOwner) { point: LatLng ->
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 14f))

        }

        viewModel.zipCode.observe(viewLifecycleOwner) {
            if (it.length == 5) {
                viewModel.getPolygonCoordinates(it)
            }
        }
    }

    private fun updatePolygon(points: List<LatLng>) {
        currentPolygon?.remove()
        val polygonOptions = PolygonOptions().run {
            addAll(points)
            strokeColor(Color.BLACK)
            fillColor(R.color.black_light)
            geodesic(true)
        }
        currentPolygon = map.addPolygon(polygonOptions)
    }
}