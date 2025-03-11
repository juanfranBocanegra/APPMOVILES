package com.app.dolt.repository

import com.app.dolt.api.RetrofitClient

class PostRepository {
    private val api = RetrofitClient.apiService

    suspend fun getFeed(size: Int) = api.getFeed(size)
    //suspend fun getPostById(id: Int) = api.getPostById(id)
}