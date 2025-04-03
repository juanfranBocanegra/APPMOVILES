package com.app.dolt.repository

import com.app.dolt.api.RetrofitClient
import com.app.dolt.model.UserProfile
import retrofit2.HttpException
import java.io.IOException

/**
 * Repositorio encargado de gestionar las operaciones relacionadas con los perfiles de usuario.
 */
class ProfileRepository {
    private val apiService = RetrofitClient.apiService

    /**
     * Obtiene el perfil de un usuario específico desde la API.
     * 
     * @param username : Nombre de usuario de cuyo perfil se desea obtener.
     * @return Objeto [UserProfile] si la respuesta es exitosa, o 'null' si no hay datos.
     * @throws Excepción : Si ocurre un error de red o un error HTTP.
     */
    suspend fun getProfile(username: String) :UserProfile? {
        return try {
            val response = apiService.getProfile(username)
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