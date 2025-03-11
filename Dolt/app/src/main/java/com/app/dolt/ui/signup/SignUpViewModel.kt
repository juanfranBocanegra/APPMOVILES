package com.app.dolt.ui.signup

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dolt.api.RetrofitClient
import com.app.dolt.model.LoginRequest
import com.app.dolt.model.LoginResponse
import com.app.dolt.model.SignUpRequest
import kotlinx.coroutines.launch
import retrofit2.Response

class SignUpViewModel() : ViewModel() {

    // Método para realizar la solicitud de login
    fun signup(username: String, name: String, password: String, password2: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val signupRequest = SignUpRequest(username,name, password, password2)
                val response: Response<Unit> = RetrofitClient.apiService.signup(signupRequest)

                if (response.isSuccessful) {
                    onSuccess("OK")  // Llamar al callback de éxito con el token

            } else {
            onError("Registro fallido: ${response.message()}")
            }
            } catch (e: Exception) {
                onError("Error en la conexión: ${e.message}")
            }
        }
    }


}
