package com.app.dolt.api

import com.app.dolt.model.Challenge
import com.app.dolt.model.FollowRequest
import com.app.dolt.model.FollowResponse
import com.app.dolt.model.LoginRequest
import com.app.dolt.model.LoginResponse
import com.app.dolt.model.LogoutRequest
import com.app.dolt.model.UserProfile
import com.app.dolt.model.Post
import com.app.dolt.model.PostRequest
import com.app.dolt.model.SignUpRequest
import com.app.dolt.model.UserSimple
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    @POST("signup/")
    suspend fun signup(
        @Body signupRequest: SignUpRequest
    ): Response<Unit>

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
    suspend fun getProfile(@Path("username") username: String): Response<UserProfile>

    @GET("feed/{size}")
    suspend fun getFeed(@Path("size") size: Int): List<Post>

    @GET("follow/")
    suspend fun getFollow() : Response<FollowResponse>

    @POST("follow/")
    suspend fun follow(
        @Body followRequest : FollowRequest
    ) : Response<Unit>

    @POST("unfollow/")
    suspend fun unfollow(
        @Body followRequest : FollowRequest
    ) : Response<Unit>

    @GET("search/{text}")
    suspend fun search(@Path("text") text: String): List<UserSimple>

    @POST("post/")
    suspend fun post(
        @Body postRequest: PostRequest
    ): Response<Unit>

    //@GET("posts/{id}")
    //suspend fun getPostById(@Path("id") id: Int): Challenge
}
