package com.yawpie.rotidigitalent.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawpie.rotidigitalent.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun registerUser(username: String, password: String) {
        viewModelScope.launch {
            userRepository.registerUser(username, password)
        }
    }
}