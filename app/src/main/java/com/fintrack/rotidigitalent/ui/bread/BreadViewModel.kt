package com.fintrack.rotidigitalent.ui.bread

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.rotidigitalent.data.Bread
import com.fintrack.rotidigitalent.data.repository.BreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreadViewModel @Inject constructor(
    private val breadRepository: BreadRepository,

): ViewModel() {

    private val _breads = MutableLiveData<List<Bread>>()
    val breads: LiveData<List<Bread>> = _breads

    fun getAllBreads(callback: () -> Unit) {
        viewModelScope.launch {
            try {
                val listBreadItems = breadRepository.getAllBreads()
                _breads.postValue(listBreadItems)
                callback()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}