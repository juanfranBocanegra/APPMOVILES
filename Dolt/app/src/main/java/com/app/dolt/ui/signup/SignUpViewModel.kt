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

/**
 * ViewModel encargado de gestionar la lógica de registro de nuevos usuarios.
 * Realiza la solicitud de registro a través del servicio API.
 */
class SignUpViewModel() : ViewModel() {

    /**
     * Realiza la solicitud de registro de un nuevo usuario.
     *
     * @param username : Nombre de usuario.
     * @param name : Nombre visible del usuario.
     * @param password : Contraseña del usuario.
     * @param password2 : Confirmación de la contraseña.
     * @param onSuccess : Función que se ejecuta si el registro es exitoso.
     * @param onError : Función que se ejecuta si ocurre un error.
     */
    fun signup(username: String, name: String, password: String, password2: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val signupRequest = SignUpRequest(username,name, password, password2)
                val response: Response<Unit> = RetrofitClient.apiService.signup(signupRequest)

                if (response.isSuccessful) {
                    onSuccess("OK")  // Llamar al callback de éxito
                } else {
                    onError("Registro fallido: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error en la conexión: ${e.message}")
            }
        }
    }
}
