package com.app.dolt.repository

import com.app.dolt.api.RetrofitClient

/**
 * Repositorio encargado de gestionar las operaciones relacionadas con los desafíos.
 */
class ChallengeRepository {
    private val api = RetrofitClient.apiService

    /**
     * Obtiene la lista de desafíos disponibles desde la API.
     * 
     * @return Lista de objetos [Challenge].
     */
    suspend fun getChallenges() = api.getChallenges()

    // Método para obtener un post por ID (actualmente no utilizado).
    //suspend fun getPostById(id: Int) = api.getPostById(id)
}