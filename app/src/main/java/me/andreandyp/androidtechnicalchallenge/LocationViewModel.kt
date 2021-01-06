package me.andreandyp.androidtechnicalchallenge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import me.andreandyp.androidtechnicalchallenge.network.NetworkResponse
import me.andreandyp.androidtechnicalchallenge.repository.AppRepository
import me.andreandyp.androidtechnicalchallenge.repository.models.PolygonCoordinates
import me.andreandyp.androidtechnicalchallenge.repository.models.ZipCodeSettlements

class LocationViewModel : ViewModel() {
    private val _geoPoints = MutableLiveData<List<LatLng>>()
    val geoPoints: LiveData<List<LatLng>>
        get() = _geoPoints

    val zipCode = MutableLiveData<String>()

    private val _centerPoint = MutableLiveData<LatLng>()
    val centerPoint: LiveData<LatLng>
        get() = _centerPoint

    private val _settlements = MutableLiveData<ZipCodeSettlements>()
    val settlements: LiveData<ZipCodeSettlements>
        get() = _settlements

    private val repository = AppRepository()

    fun onReadyMap() {
        val downtown = LatLng(19.432723850153973, -99.13313084558676)
        _centerPoint.value = downtown
    }

    fun getPolygonCoordinates(zipCode: String) {
        viewModelScope.launch {
            when (val response = repository.getPolygonCoordinates(zipCode)) {
                is NetworkResponse.Success -> {
                    val polygonCoordinates = response.data as PolygonCoordinates
                    _geoPoints.value = polygonCoordinates.coordinates
                }
                is NetworkResponse.Error -> {
                }
                is NetworkResponse.NetworkError -> {
                }
            }
        }
    }

    fun getPolygonsDetails(zipCode: String) {
        viewModelScope.launch {
            when (val response = repository.getSettlementsOfZipCode(zipCode)) {
                is NetworkResponse.Success -> {
                    _settlements.value = response.data as ZipCodeSettlements
                }
                is NetworkResponse.Error -> {
                }
                is NetworkResponse.NetworkError -> {
                }
            }
        }
    }
}