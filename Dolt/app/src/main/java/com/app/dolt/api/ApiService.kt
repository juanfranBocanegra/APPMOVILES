package com.app.dolt.api

import com.app.dolt.model.Challenge
import com.app.dolt.model.LoginRequest
import com.app.dolt.model.LoginResponse
import com.app.dolt.model.LogoutRequest
import com.app.dolt.model.Profile
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    @POST("login/")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("logout/")
    suspend fun logout(
        @Body logoutRequest: LogoutRequest
    ): Response<Unit>

    @GET("check/") // Endpoint para validar el token
    suspend fun validateToken(): Response<Unit>

    @GET("challenges/")
    suspend fun getChallenges(): List<Challenge>

    @GET("profile/{username}")
    suspend fun getProfile(@Path("username") username: String): Response<Profile>

    //@GET("posts/{id}")
    //suspend fun getPostById(@Path("id") id: Int): Challenge
}
