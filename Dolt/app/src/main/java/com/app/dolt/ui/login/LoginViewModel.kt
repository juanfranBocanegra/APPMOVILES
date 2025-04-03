package com.app.dolt.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dolt.api.RetrofitClient
import com.app.dolt.model.LoginRequest
import com.app.dolt.model.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Response


/**
 * ViewModel encargado de gestionar la lógica de inicio de sesión.
 * Realiza la solicitud de login y gestiona el almacenamiento de los tokens.
 *
 * @property sharedPreferences : Referencia a las preferencias compartidas para almacenar tokens.
 */
class LoginViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    /**
     * Realiza la solicitud de login y guarda los tokens si la autenticación es exitosa.
     *
     * @param username : Nombre de usuario.
     * @param password : Contraseña del usuario.
     * @param onSuccess : Función que se ejecuta si el login es exitoso (devuelve el token).
     * @param onError : Función que se ejecuta si ocurre un error.
     */
    fun login(username: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(username, password)
                val response: Response<LoginResponse> = RetrofitClient.apiService.login(loginRequest)

                if (response.isSuccessful) {
                    val token = response.body()?.access
                    val refresh = response.body()?.refresh
                    if (token != null && refresh != null) {
                        saveAccessToken(token)
                        saveRefreshToken(refresh)
                        onSuccess(token) 
                        saveUsername(username)
                    } else onError("Token no disponible")
                } else {
                    onError("Login fallido: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error en la conexión: ${e.message}")
            }
        }
    }

    /**
     * Guarda el token de acceso en las preferencias compartidas.
     *
     * @param token : Token de acceso.
     */
    private fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString("ACCESS_TOKEN", token).apply()
    }

    /**
     * Guarda el token de actualización en las preferencias compartidas.
     *
     * @param token : Token de actualización.
     */
    private fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString("REFRESH_TOKEN", token).apply()
    }

    /**
     * Guarda el nombre de usuario en las preferencias compartidas.
     *
     * @param username : Nombre de usuario.
     */    
    private fun saveUsername(username: String) {
        sharedPreferences.edit().putString("USERNAME", username).apply()
    }

    /**
     * Obtiene el token de acceso almacenado.
     *
     * @return Token de acceso o `null` si no existe.
     */
    fun getToken(): String? {
        return sharedPreferences.getString("ACCESS_TOKEN", null)
    }
}
