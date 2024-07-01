package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.api.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.api.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch

class SignupViewModel (private val repository:UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.register(name, email, password)
                if (response.isSuccessful) {
                    _registerResponse.postValue(response.body())
                } else {
                    val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    _registerResponse.postValue(RegisterResponse(error = errorResponse?.error, message = errorResponse?.message))
                }
            } catch (e: Exception) {
                _registerResponse.postValue(RegisterResponse(message = e.localizedMessage, error = true))
            } finally {
                _isLoading.value = false
            }
        }
    }
}