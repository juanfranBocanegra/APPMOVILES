package com.app.dolt.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dolt.api.RetrofitClient
import com.app.dolt.model.LoginRequest
import com.app.dolt.model.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    // Método para realizar la solicitud de login
    fun login(username: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(username, password)
                val response: Response<LoginResponse> = RetrofitClient.apiService.login(loginRequest)

                if (response.isSuccessful) {
                    val token = response.body()?.access
                    token?.let {
                        saveToken(it)
                        onSuccess(it)  // Llamar al callback de éxito con el token
                        saveUsername(username)
                    } ?: onError("Token no disponible")
                } else {
                    onError("Login fallido: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error en la conexión: ${e.message}")
            }
        }
    }

    // Guardar el token en SharedPreferences
    private fun saveToken(token: String) {
        sharedPreferences.edit().putString("BEARER_TOKEN", token).apply()

    }

    private fun saveUsername(username: String) {
        sharedPreferences.edit().putString("USERNAME", username).apply()

    }



    // Obtener el token guardado
    fun getToken(): String? {
        return sharedPreferences.getString("BEARER_TOKEN", null)
    }
}
