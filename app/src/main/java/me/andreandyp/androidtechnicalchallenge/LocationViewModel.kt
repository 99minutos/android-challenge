package me.andreandyp.androidtechnicalchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import me.andreandyp.androidtechnicalchallenge.repository.AppRepository

class LocationViewModel : ViewModel() {
    private val _geoPoints = MutableLiveData<List<LatLng>>()
    val geoPoints: LiveData<List<LatLng>>
        get() = _geoPoints

    val zipCode = MutableLiveData<String>()

    private val _centerPoint = MutableLiveData<LatLng>()
    val centerPoint: LiveData<LatLng>
        get() = _centerPoint

    private val repository = AppRepository()

    fun onReadyMap() {
        val downtown = LatLng(19.432723850153973, -99.13313084558676)
        _centerPoint.value = downtown
    }

    fun getPolygonCoordinates(zipCode: String) {
        viewModelScope.launch {
            val coordinates: List<LatLng> = repository.getPolygonCoordinates(zipCode).coordinates
            _geoPoints.value = coordinates
        }
    }
}