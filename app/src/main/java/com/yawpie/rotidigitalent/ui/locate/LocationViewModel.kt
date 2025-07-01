package com.yawpie.rotidigitalent.ui.locate

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawpie.rotidigitalent.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val application: Application
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _latitude = MutableLiveData<Float>()
    val latitude: LiveData<Float> = _latitude
    private val _longitude = MutableLiveData<Float>()
    val longitude: LiveData<Float> = _longitude

    fun updateLocation() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            delay(1000L)
            try {
                var latitude: Float? = null
                var longitude: Float? = null
                userRepository.getCurrentLocation(application) { lat, long ->
                    _latitude.postValue(lat!!)
                    _longitude.postValue(long!!)
                    latitude = lat
                    longitude = long
                }
                userRepository.setLastKnownLocation(latitude!!, longitude!!)

            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error fetching location: ${e.message}")
            } finally {
                _isLoading.postValue(false)

            }
        }
    }

    fun getLastKnownLocation() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                userRepository.getLastKnownLocation().onSuccess {
                    _latitude.postValue(it?.latitude?.toFloat())
                    _longitude.postValue(it?.longitude?.toFloat())
                }.onFailure {
                    Log.e("LocationViewModel", "Error fetching location: ${it.message}")
                }

            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error fetching location: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }

        }
    }
}