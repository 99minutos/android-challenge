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
    private lateinit var errorDialog: AlertDialog
    private lateinit var errorNetworkDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.location_fragment, container, false)
        binding.lifecycleOwner = this
        loadingPolygonsDialog = createLoadingDialog(R.layout.loading_polygon_dialog)
        loadingSettlementsDialog = createLoadingDialog(R.layout.loading_settlements_dialog)
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
            viewModel.getPolygonCoordinates(zipCode)
            viewModel.getPolygonsDetails(zipCode)
            val imm: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        }
    }
}