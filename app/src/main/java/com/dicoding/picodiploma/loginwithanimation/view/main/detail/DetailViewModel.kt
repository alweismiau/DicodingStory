package com.dicoding.picodiploma.loginwithanimation.view.main.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.api.StoryResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val apiService: ApiService) : ViewModel() {
    val storyDetail = MutableLiveData<StoryResponse>()

    fun fetchStoryDetail( token: String, storyId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getStoryDetail(token, storyId)
                storyDetail.postValue(response)
            } catch (e: Exception) {
                storyDetail.postValue(StoryResponse(message = e.localizedMessage, error = true.toString()))
            }
        }
    }
}

