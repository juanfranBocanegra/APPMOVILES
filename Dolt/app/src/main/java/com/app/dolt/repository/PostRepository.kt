package com.app.dolt.repository

import com.app.dolt.api.RetrofitClient

/**
 * Repositorio encargado de gestionar las operaciones relacionadas con las publicaciones.
 */
class PostRepository {
    private val api = RetrofitClient.apiService

    /**
     * Obtiene el feed de publicaciones desde la API.
     * 
     * @param size : Número de publicaciones a obtener.
     * @return Lista de objetos [Post].
     */
    suspend fun getFeed(size: Int) = api.getFeed(size)

    // Método para obtener un post por ID (actualmente no utilizado).
    //suspend fun getPostById(id: Int) = api.getPostById(id)
}