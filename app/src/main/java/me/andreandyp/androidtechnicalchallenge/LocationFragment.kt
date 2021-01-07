package me.andreandyp.androidtechnicalchallenge

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import me.andreandyp.androidtechnicalchallenge.databinding.LocationFragmentBinding
import me.andreandyp.androidtechnicalchallenge.network.NetworkResponse
import me.andreandyp.androidtechnicalchallenge.repository.models.ZipCodeSettlements

class LocationFragment : Fragment() {
    private lateinit var viewModel: LocationViewModel
    private lateinit var binding: LocationFragmentBinding
    private lateinit var loadingPolygonsDialog: AlertDialog
    private lateinit var loadingSettlementsDialog: AlertDialog
    private lateinit var errorPolygonDialog: AlertDialog
    private lateinit var errorSepomexDialog: AlertDialog
    private lateinit var errorServerDialog: AlertDialog
    private lateinit var errorNetworkDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.location_fragment, container, false)
        binding.lifecycleOwner = this
        loadingPolygonsDialog = createLoadingDialog(R.layout.loading_polygon_dialog)
        loadingSettlementsDialog = createLoadingDialog(R.layout.loading_settlements_dialog)
        errorPolygonDialog = createDialog(
            R.string.polygon_error,
            R.string.ok,
            null
        ) { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.cancel()
        }
        errorSepomexDialog = createDialog(
            R.string.sepomex_error,
            R.string.ok,
            null
        ) { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.cancel()
        }
        errorServerDialog = createDialog(
            R.string.server_error,
            R.string.retry,
            R.string.cancel
        ) { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.cancel()
            val currentZipCode = viewModel.zipCode.value
            currentZipCode?.let {
                getZipCodeInformation(it)
            }
        }
        errorNetworkDialog = createDialog(
            R.string.network_error,
            R.string.retry,
            R.string.cancel
        ) { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.cancel()
            val currentZipCode = viewModel.zipCode.value
            currentZipCode?.let {
                getZipCodeInformation(it)
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)
        binding.vm = viewModel

        viewModel.zipCode.observe(viewLifecycleOwner) { getZipCodeInformation(it) }

        viewModel.settlements.observe(viewLifecycleOwner) { updateSettlements(it) }

        viewModel.statusPolygons.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> loadingPolygonsDialog.show()
                is NetworkResponse.Error -> {
                    loadingPolygonsDialog.cancel()
                    if (it.statusCode == 500) {
                        errorPolygonDialog.show()
                    } else {
                        errorServerDialog.show()
                    }
                }
                is NetworkResponse.NetworkError -> {
                    loadingPolygonsDialog.cancel()
                    errorNetworkDialog.show()
                }
                else -> loadingPolygonsDialog.cancel()
            }
        }
        viewModel.statusSettlements.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> loadingSettlementsDialog.show()
                is NetworkResponse.Error -> {
                    loadingSettlementsDialog.cancel()
                    if (it.statusCode == 404) {
                        errorSepomexDialog.show()
                    } else {
                        errorServerDialog.show()
                    }
                }
                is NetworkResponse.NetworkError -> {
                    loadingSettlementsDialog.cancel()
                    errorNetworkDialog.show()
                }
                else -> loadingSettlementsDialog.cancel()
            }
        }
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
        negativeId: Int?,
        action: ((DialogInterface, Int) -> Unit)
    ): AlertDialog {
        return AlertDialog.Builder(requireContext()).run {
            setMessage(messageId)
            setPositiveButton(positiveId, action)
            negativeId?.let {
                setNegativeButton(negativeId) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.cancel()
                }
            }
            create()
        }
    }

    private fun getZipCodeInformation(zipCode: String) {
        if (zipCode.length == 5) {
            viewModel.getPolygonCoordinates(zipCode)
            viewModel.getPolygonsDetails(zipCode)
            val imm: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        }
    }
}