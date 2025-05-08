package com.app.dolt.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.util.Log
import com.app.dolt.model.Post
import com.app.dolt.repository.PostRepository

/**
 * ViewModel encargado de gestionar la lógica y datos relacionados con las publicaciones.
 * Recupera la lista de publicaciones desde el repositorio y expone el estado mediante [StateFlow].
 */
class PostViewModel : ViewModel() {

    private val repository = PostRepository()
    private val _posts = MutableStateFlow<List<Post>>(emptyList())

    /**
     * Estado observable que contiene la lista actual de publicaciones.
     * La interfaz puede suscribirse a este flujo para recibir actualizaciones.
     */    
    val posts: StateFlow<List<Post>> = _posts

    /**
     * Carga la lista de publicaciones desde el repositorio.
     *
     * @param size : Número de publicaciones a obtener.
     */    
    fun loadPosts(size: Int) {
        viewModelScope.launch {
            try {
                repository.refreshPosts()
                _posts.value = repository.getLocalFeed(size)
            } catch (e: Exception) {
                Log.i("ERROR",e.toString())
            }
        }
    }

    suspend fun refreshPosts(){
        repository.refreshPosts()
    }
}
