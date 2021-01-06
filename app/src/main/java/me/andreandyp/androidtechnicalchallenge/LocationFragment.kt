package me.andreandyp.androidtechnicalchallenge

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import me.andreandyp.androidtechnicalchallenge.network.NetworkResponse
import me.andreandyp.androidtechnicalchallenge.repository.models.ZipCodeSettlements

class LocationFragment : Fragment() {

    private lateinit var viewModel: LocationViewModel
    private lateinit var mapPolygon: SupportMapFragment
    private lateinit var binding: LocationFragmentBinding
    private lateinit var loadingPolygonsDialog: AlertDialog
    private lateinit var loadingSettlementsDialog: AlertDialog
    private lateinit var errorDialog: AlertDialog
    private lateinit var errorNetworkDialog: AlertDialog
    private var map: GoogleMap? = null
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
        loadingPolygonsDialog = createLoadingDialog(R.layout.loading_polygon_dialog)
        loadingSettlementsDialog = createLoadingDialog(R.layout.loading_settlements_dialog)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        binding.vm = viewModel

        viewModel.geoPoints.observe(viewLifecycleOwner) { updatePolygon(it) }

        viewModel.centerPoint.observe(viewLifecycleOwner) { point: LatLng ->
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 14f))
        }

        viewModel.zipCode.observe(viewLifecycleOwner) { getZipCodeInformation(it) }

        viewModel.settlements.observe(viewLifecycleOwner) { updateSettlements(it) }

        viewModel.statusPolygons.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> {
                    loadingPolygonsDialog.show()
                }
                else -> {
                    loadingPolygonsDialog.cancel()
                }
            }
        }
        viewModel.statusSettlements.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> {
                    loadingSettlementsDialog.show()
                }
                else -> {
                    loadingSettlementsDialog.cancel()
                }
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
        currentPolygon = map?.addPolygon(polygonOptions)
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
        autoCompleteTextView.setText("")
        countryEditText.setText(R.string.country_default)
    }

    private fun createLoadingDialog(layoutId: Int): AlertDialog {
        return AlertDialog.Builder(requireContext()).run {
            setIcon(android.R.drawable.progress_horizontal)
            setView(layoutId)
            setCancelable(false)
            create()
        }
    }

    private fun createDialog(
        messageId: Int,
        positiveId: Int,
        negativeId: Int,
        action: ((DialogInterface, Int) -> Unit)
    ): AlertDialog {
        return AlertDialog.Builder(requireContext()).run {
            setMessage(messageId)
            setPositiveButton(positiveId, action)
            setNegativeButton(negativeId) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.cancel()
            }
            create()
        }
    }

    private fun getZipCodeInformation(zipCode: String) {
        if (zipCode.length == 5) {
            val imm: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            viewModel.getPolygonCoordinates(zipCode)
            viewModel.getPolygonsDetails(zipCode)
        }
    }
}