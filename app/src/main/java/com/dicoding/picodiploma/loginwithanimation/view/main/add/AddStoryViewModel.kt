package com.dicoding.picodiploma.loginwithanimation.view.main.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.api.FileUploadResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uploadStatus = MutableLiveData<FileUploadResponse>()
    val uploadStatus: LiveData<FileUploadResponse> get() = _uploadStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val tokenLiveData: LiveData<String?> = liveData {
        userRepository.getTokenFlow().collect {
            emit(it)
        }
    }

    fun uploadStory(image: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            val token = userRepository.getToken() // Asynchronously get the token
            if (token != null && token.isNotEmpty()) {
                val formattedToken = "Bearer $token"
                _isLoading.postValue(true)
                ApiConfig.getApiService().uploadImage(formattedToken, image, description).enqueue(object : Callback<FileUploadResponse> {
                    override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
                        if (response.isSuccessful) {
                            _uploadStatus.postValue(response.body())
                        } else {
                            _uploadStatus.postValue(FileUploadResponse(error = true, message = "Failed to upload story: ${response.errorBody()?.string()}"))
                        }
                        _isLoading.postValue(false)
                    }

                    override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                        _uploadStatus.postValue(FileUploadResponse(error = true, message = "Failed to upload story: ${t.message}"))
                        _isLoading.postValue(false)
                    }
                })
            } else {
                _uploadStatus.postValue(FileUploadResponse(error = true, message = "Authentication token is not available or invalid."))
            }
        }
    }

}
