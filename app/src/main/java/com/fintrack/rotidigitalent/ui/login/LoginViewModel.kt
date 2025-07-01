package com.fintrack.rotidigitalent.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.rotidigitalent.data.User
import com.fintrack.rotidigitalent.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.loginUser(username, password)
            _loginResult.postValue(result)
        }
    }
}