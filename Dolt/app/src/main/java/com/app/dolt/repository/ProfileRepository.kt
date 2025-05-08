package com.app.dolt.repository

import android.content.Context
import com.app.dolt.api.RetrofitClient
import com.app.dolt.data.AppDatabase
import com.app.dolt.data.ChallengeDao
import com.app.dolt.model.UserProfile
import com.app.dolt.ui.login.UnauthorizedLoginException
import retrofit2.HttpException
import java.io.IOException



/**
 * Repositorio encargado de gestionar las operaciones relacionadas con los perfiles de usuario.
 */
class ProfileRepository(context: Context) {
    private val api = RetrofitClient.apiService
    private val db = AppDatabase.getDatabase(context)
    /**
     * Obtiene el perfil de un usuario específico desde la API.
     * 
     * @param username : Nombre de usuario de cuyo perfil se desea obtener.
     * @return Objeto [UserProfile] si la respuesta es exitosa, o 'null' si no hay datos.
     * @throws Excepción : Si ocurre un error de red o un error HTTP.
     */
    suspend fun refreshProfile(username: String) {
        try {
            val response = api.getProfile(username)
            if (response.isSuccessful) {
                val profile = response.body()
                if (profile != null) {
                    db.userProfileDao().upsertUserProfile(profile) // Devuelve el perfil si la respuesta es exitosa
                }
            } else if(response.code() == 401){
                throw UnauthorizedLoginException()
            }
            else {
                throw HttpException(response) // Lanza una excepción si hay error HTTP
            }
        } catch (e: IOException) {
            // Error de red
            //throw Exception("Error de conexión: ${e.message}")
        } catch (e: HttpException) {
            // Error HTTP (código 400 o 500)
            //throw Exception("Error en la API: ${e.message}")
        }
    }

    suspend fun getLocalProfile(username: String) : UserProfile? = db.userProfileDao().getUserProfile(username)
}