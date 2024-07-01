package com.dicoding.picodiploma.loginwithanimation.data

import android.content.Context
import android.util.Log
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.api.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.api.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        val response = apiService.login(email, password)
        if (response.isSuccessful && response.body() != null) {
            val token = response.body()?.loginResult?.token
            if (token != null) {
                saveToken(token)
            }
        }
        return response
    }

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Response<RegisterResponse> {
        return apiService.register(name, email, password)
    }

    private suspend fun saveToken(token: String) {
        userPreference.saveSession(UserModel("", token, true))
        Log.d("Login", "Token saved: $token")
    }

    fun getTokenFlow(): Flow<String?> = userPreference.getSession().map { it.token }

    suspend fun getToken(): String? {
        return withContext(Dispatchers.IO) {
            userPreference.getSession().first().token
        }
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
