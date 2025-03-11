package com.app.dolt.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.util.Log
import com.app.dolt.model.Post
import com.app.dolt.repository.PostRepository

class PostViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    fun loadPosts(size: Int) {
        viewModelScope.launch {
            try {
                _posts.value = repository.getFeed(size)
            } catch (e: Exception) {
                Log.i("ERROR",e.toString())
            }
        }
    }
}
