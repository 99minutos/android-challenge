package me.andreandyp.androidtechnicalchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
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
            val polygonCoordinates: PolygonCoordinates = repository.getPolygonCoordinates(zipCode)
            _geoPoints.value = polygonCoordinates.coordinates
        }
    }

    fun getPolygonsDetails(zipCode: String) {
        viewModelScope.launch {
            val settlements: ZipCodeSettlements = repository.getSettlementsOfZipCode(zipCode)
            _settlements.value = settlements
        }
    }
}