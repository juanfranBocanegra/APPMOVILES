package com.app.dolt.ui.challenge.vote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.util.Log
import com.app.dolt.api.RetrofitClient
import com.app.dolt.model.Challenge
import com.app.dolt.model.Post
import com.app.dolt.model.VoteResponse
import com.app.dolt.repository.PostRepository
import timber.log.Timber

/**
 * ViewModel encargado de gestionar la lógica y datos relacionados con las publicaciones.
 * Recupera la lista de publicaciones desde el repositorio y expone el estado mediante [StateFlow].
 */
class VoteViewModel : ViewModel() {


    private val _posts = MutableStateFlow<List<Post>>(emptyList())



    private val api = RetrofitClient.apiService

    /**
     * Estado observable que contiene la lista actual de publicaciones.
     * La interfaz puede suscribirse a este flujo para recibir actualizaciones.
     */    
    val posts: StateFlow<List<Post>> = _posts

    var challenge: Challenge? = null

    /**
     * Carga la lista de publicaciones desde el repositorio.
     *
     * @param size : Número de publicaciones a obtener.
     */    
    fun loadVote() {
        viewModelScope.launch {
            try {
                val response = api.getVote()
                if (response.isSuccessful) {
                    val vote = response.body()
                    if (vote != null) {
                        Timber.d("Challengeeeeeeeeeeeeeeeeeeeeeeee: ${vote.challenge}")
                        challenge = vote.challenge
                        _posts.value = vote.posts
                    }
                } else {
                    Timber.tag("ERROR").i("Error en la respuesta de la API")
                }
            } catch (e: Exception) {
                Timber.tag("ERROR").i(e.toString())
            }
        }
    }

    fun sendVote(posts: MutableList<Post>) {
        viewModelScope.launch {
            challenge?.let {
                val request = VoteResponse(
                    challenge = it,
                    posts = posts
                )
                api.sendVote(request)
            }
        }
    }


}
