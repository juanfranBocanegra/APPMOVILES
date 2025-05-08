package com.app.dolt.repository

import com.app.dolt.MyApplication
import com.app.dolt.api.RetrofitClient
import com.app.dolt.data.AppDatabase
import com.app.dolt.model.UserStats
import com.app.dolt.ui.login.UnauthorizedLoginException
import retrofit2.HttpException
import java.io.IOException

class UserStatsRepository {
    private val api = RetrofitClient.apiService
    private val db = AppDatabase.getDatabase(MyApplication.appContext)

    /**
     * Actualiza las estadísticas del usuario desde la API.
     *
     * @param username : Nombre de usuario del cual se desean obtener las estadísticas.
     * @return Objeto [UserStats] con las estadísticas del usuario.
     */
    suspend fun refreshUserStats(): UserStats? {
        try {
            val response = api.getUserStats()
            if (response.isSuccessful) {
                val userStats = response.body()
                if (userStats != null) {
                    db.userStatsDao().deleteAllUserStats()
                    db.userStatsDao().insertUserStats(userStats)
                    return userStats
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
        return null
    }

    suspend fun getLocalUserStats(): UserStats? = db.userStatsDao().getUserStats()

}