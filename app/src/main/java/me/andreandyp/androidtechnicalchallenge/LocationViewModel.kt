package me.andreandyp.androidtechnicalchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class LocationViewModel : ViewModel() {
    private val _geoPoints = MutableLiveData<List<LatLng>>()
    val geoPoints: LiveData<List<LatLng>>
        get() = _geoPoints

    fun onReadyMap() {
        val hidalgo = LatLng(19.444747420622267, -99.14502638852542)
        val morelos = LatLng(19.43976245268299, -99.11815379002334)
        val candelaria = LatLng(19.42958433453182, -99.12062078267273)
        val balderas = LatLng(19.42725781796235, -99.14899119814048)

        _geoPoints.value = listOf(hidalgo, morelos, candelaria, balderas)
    }
}