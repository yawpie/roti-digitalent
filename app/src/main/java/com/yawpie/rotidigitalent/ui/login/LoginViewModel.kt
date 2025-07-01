package com.yawpie.rotidigitalent.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawpie.rotidigitalent.data.User
import com.yawpie.rotidigitalent.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(username: String, password: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val result = userRepository.loginUser(username, password)
                _loginResult.postValue(result)

            } catch (e: Exception) {
                e.printStackTrace()
                _loginResult.postValue(Result.failure(e))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getUser() {

        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val user = userRepository.getUser().getOrNull()
                if (user == null) {
                    _user.postValue(null)
                } else {
                    _user.postValue(user)
                }
            } catch (e: Exception) {
                e.printStackTrace()

            } finally {
                _isLoading.postValue(false)
            }
        }

    }

    fun logout() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                delay(2000L)
                userRepository.logout()
                _user.postValue(null)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

}