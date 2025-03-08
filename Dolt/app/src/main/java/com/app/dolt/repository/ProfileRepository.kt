package com.app.dolt.repository

import com.app.dolt.api.RetrofitClient
import com.app.dolt.model.LoginRequest
import com.app.dolt.model.ProfileRequest
import com.app.dolt.model.ProfileResponse
import retrofit2.HttpException
import java.io.IOException

class ProfileRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getProfile(username: String) :ProfileResponse? {
        return try {
            val response = apiService.getProfile(ProfileRequest(username))
            if (response.isSuccessful) {
                response.body() // Devuelve el perfil si la respuesta es exitosa
            } else {
                throw HttpException(response) // Lanza una excepción si hay error HTTP
            }
        } catch (e: IOException) {
            // Error de red
            throw Exception("Error de conexión: ${e.message}")
        } catch (e: HttpException) {
            // Error HTTP (código 400 o 500)
            throw Exception("Error en la API: ${e.message}")
        }
    }
}