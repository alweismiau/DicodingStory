package com.dicoding.picodiploma.loginwithanimation.view.maps

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.StoryResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {
        val storiesWithLocation = MutableLiveData<StoryResponse>()

        fun fetchStoriesWithLocation() {
                viewModelScope.launch {
                        try {
                                val response = repository.getStoriesWithLocation()
                                storiesWithLocation.postValue(response)
                        } catch (e: Exception) {
                                // Handle the exception, e.g., set a value in an error LiveData
                        }
                }
        }
}