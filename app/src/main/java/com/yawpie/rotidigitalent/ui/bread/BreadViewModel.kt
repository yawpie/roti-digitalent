package com.yawpie.rotidigitalent.ui.bread

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawpie.rotidigitalent.data.Bread
import com.yawpie.rotidigitalent.data.repository.BreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreadViewModel @Inject constructor(
    private val breadRepository: BreadRepository,

    ) : ViewModel() {

    private val _breads = MutableLiveData<List<Bread>>()
    val breads: LiveData<List<Bread>> = _breads
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _uploadSuccess = MutableLiveData<Event<Boolean>>()
    val uploadSuccess: LiveData<Event<Boolean>> = _uploadSuccess

    fun fetchBreads() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                delay(2000L)
                val response = breadRepository.getAllBreads()
                _breads.postValue(response)

            } catch (e: Exception) {
                Log.e("ItemViewModel", "Error fetching items: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun addBread(name: String, description: String, imageUriString: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                breadRepository.addBreadWithImageUpload(
                    name, description,
                    imageUriString.toUri()
                ).onSuccess {
                    _uploadSuccess.postValue(Event(true))
                }.onFailure {
                    throw Exception("Failed to upload")
                }
            } catch (e: Exception) {
                _uploadSuccess.postValue(Event(false))
                Log.e("AddBreadActivity", "Error adding bread: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}