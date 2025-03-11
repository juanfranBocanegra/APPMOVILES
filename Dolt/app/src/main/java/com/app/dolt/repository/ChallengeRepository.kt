package com.app.dolt.repository

import com.app.dolt.api.RetrofitClient

class ChallengeRepository {
    private val api = RetrofitClient.apiService

    suspend fun getChallenges() = api.getChallenges()
    //suspend fun getPostById(id: Int) = api.getPostById(id)
}