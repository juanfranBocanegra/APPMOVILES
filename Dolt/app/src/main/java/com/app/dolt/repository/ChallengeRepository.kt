package com.app.dolt.repository

import android.content.Context
import com.app.dolt.MyApplication
import com.app.dolt.api.RetrofitClient
import com.app.dolt.data.AppDatabase

/**
 * Repositorio encargado de gestionar las operaciones relacionadas con los desafíos.
 */
class ChallengeRepository() {
    private val api = RetrofitClient.apiService
    private val db = AppDatabase.getDatabase(MyApplication.appContext)
    /**
     * Obtiene la lista de desafíos disponibles desde la API.
     * 
     * @return Lista de objetos [Challenge].
     */
    suspend fun refreshChallenges() {
        try {
            val response = api.getChallenges()
            if (response.isSuccessful) {
                val challenges = response.body()
                if (challenges != null) {
                    db.challengeDao().deleteAllChallenges()
                    db.challengeDao().insertChallenges(challenges)
                }
            }
        }catch (e: Exception){
            //
        }
    }

    suspend fun getLocalChallenges() = db.challengeDao().getAllChallenges()

    // Método para obtener un post por ID (actualmente no utilizado).
    //suspend fun getPostById(id: Int) = api.getPostById(id)
}