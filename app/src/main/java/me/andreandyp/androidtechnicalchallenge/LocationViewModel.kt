package me.andreandyp.androidtechnicalchallenge

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

    private val _statusPolygons = MutableLiveData<NetworkResponse<Any?>>()
    val statusPolygons: LiveData<NetworkResponse<Any?>>
        get() = _statusPolygons

    private val _statusSettlements = MutableLiveData<NetworkResponse<Any?>>()
    val statusSettlements: LiveData<NetworkResponse<Any?>>
        get() = _statusSettlements

    private val repository = AppRepository()

    fun onReadyMap() {
        val downtown = LatLng(19.432723850153973, -99.13313084558676)
        _centerPoint.value = downtown
    }

    fun getPolygonCoordinates(zipCode: String) {
        viewModelScope.launch {
            _statusPolygons.value = NetworkResponse.Loading
            val response = repository.getPolygonCoordinates(zipCode)
            when (response) {
                is NetworkResponse.Success -> {
                    val polygonCoordinates = response.data as PolygonCoordinates
                    _geoPoints.value = polygonCoordinates.coordinates
                    _centerPoint.value = polygonCoordinates.coordinates[0]
                }
                is NetworkResponse.Error -> {
                }
                is NetworkResponse.NetworkError -> {
                }
            }
            _statusPolygons.value = response
        }
    }

    fun getPolygonsDetails(zipCode: String) {
        _statusSettlements.value = NetworkResponse.Loading
        viewModelScope.launch {
            val response = repository.getSettlementsOfZipCode(zipCode)
            when (response) {
                is NetworkResponse.Success -> {
                    _settlements.value = response.data as ZipCodeSettlements
                }
                is NetworkResponse.Error -> {
                }
                is NetworkResponse.NetworkError -> {
                }
            }
            _statusSettlements.value = response
        }
    }
}