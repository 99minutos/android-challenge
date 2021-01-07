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

/**
 * ViewModel that communicates the data from the app's repository, the [LocationFragment] logic
 * and the [MainActivity] logic.
 */
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
    private val downtown = LatLng(19.432723850153973, -99.13313084558676)

    /**
     * Callback function for OnReadyMap event.
     */
    fun onReadyMap() {
        _centerPoint.value = downtown
    }

    /**
     * Gets the polygon of a zip code from the app's repository
     * and sets the map's zoom.
     * If the response is not successful, the zoom is set to downtown.
     * @param zipCode The zip code of the polygon that we want to get.
     */
    fun getPolygonCoordinates(zipCode: String) {
        viewModelScope.launch {
            _statusPolygons.value = NetworkResponse.Loading
            val response = repository.getPolygonCoordinates(zipCode)
            if (response is NetworkResponse.Success) {
                val polygonCoordinates = response.data as PolygonCoordinates
                _geoPoints.value = polygonCoordinates.coordinates
                _centerPoint.value = polygonCoordinates.coordinates[0]
            } else {
                _centerPoint.value = downtown
            }

            _statusPolygons.value = response
        }
    }

    /**
     * Gets the settlements details of the zip code from the repository.
     * If the response was not successful, a default value is set.
     * @param zipCode The zip code of which we want to get the details.
     */
    fun getPolygonsDetails(zipCode: String) {
        _statusSettlements.value = NetworkResponse.Loading
        viewModelScope.launch {
            val response = repository.getDetailsOfZipCode(zipCode)
            if (response is NetworkResponse.Success) {
                _settlements.value = response.data as ZipCodeSettlements
            } else {
                _settlements.value = ZipCodeSettlements("", "", listOf(), "")
            }

            _statusSettlements.value = response
        }
    }
}