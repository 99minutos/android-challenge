package me.andreandyp.androidtechnicalchallenge

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.textfield.TextInputEditText
import me.andreandyp.androidtechnicalchallenge.databinding.LocationFragmentBinding
import me.andreandyp.androidtechnicalchallenge.repository.models.ZipCodeSettlements

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

        viewModel.zipCode.observe(viewLifecycleOwner) { zipCode: String ->
            if (zipCode.length == 5) {
                viewModel.getPolygonCoordinates(zipCode)
                viewModel.getPolygonsDetails(zipCode)
            }
        }

        viewModel.settlements.observe(viewLifecycleOwner) { updateSettlements(it) }
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

    private fun updateSettlements(settlements: ZipCodeSettlements) {
        val autoCompleteTextView = binding.autocompleteSettlement
        val countryEditText: TextInputEditText = binding.tietCountry
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.settlement_item,
            settlements.settlements
        )
        autoCompleteTextView.setAdapter(adapter)

        countryEditText.setText(R.string.country_default)
    }
}