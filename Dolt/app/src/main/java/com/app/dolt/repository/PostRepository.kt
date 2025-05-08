package com.app.dolt.repository

import com.app.dolt.MyApplication
import com.app.dolt.api.RetrofitClient
import com.app.dolt.data.AppDatabase

/**
 * Repositorio encargado de gestionar las operaciones relacionadas con las publicaciones.
 */
class PostRepository {
    private val api = RetrofitClient.apiService
    private val db = AppDatabase.getDatabase(MyApplication.appContext)

    /**
     * Obtiene el feed de publicaciones desde la API.
     * 
     * @param size : Número de publicaciones a obtener.
     * @return Lista de objetos [Post].
     */
    suspend fun refreshPosts(){
        try {
            val response = api.getFeed(0)
            if (response.isSuccessful) {
                val posts = response.body()
                if (posts != null) {
                    db.postDao().deleteAllPosts()
                    db.postDao().insertPosts(posts)
                }
            }
        }catch (e: Exception){

        }
    }

    suspend fun getLocalFeed(size: Int) = db.postDao().getAllPosts()

    // Método para obtener un post por ID (actualmente no utilizado).
    //suspend fun getPostById(id: Int) = api.getPostById(id)
}