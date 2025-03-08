package com.app.dolt.api

import com.app.dolt.model.Challenge
import com.app.dolt.model.LoginRequest
import com.app.dolt.model.LoginResponse
import com.app.dolt.model.ProfileRequest
import com.app.dolt.model.ProfileResponse
import retrofit2.http.GET
import retrofit2.http.Path
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login/")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("challenges/")
    suspend fun getChallenges(): List<Challenge>

    @GET("profile/")
    suspend fun getProfile(
        @Body profileRequest: ProfileRequest
    ): Response<ProfileResponse>

    //@GET("posts/{id}")
    //suspend fun getPostById(@Path("id") id: Int): Challenge
}
