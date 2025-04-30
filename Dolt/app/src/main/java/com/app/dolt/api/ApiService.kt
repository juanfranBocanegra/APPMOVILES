package com.app.dolt.api

import com.app.dolt.model.Challenge
import com.app.dolt.model.FollowRequest
import com.app.dolt.model.FollowResponse
import com.app.dolt.model.GoogleLoginRequest
import com.app.dolt.model.LoginRequest
import com.app.dolt.model.LoginResponse
import com.app.dolt.model.LogoutRequest
import com.app.dolt.model.UserProfile
import com.app.dolt.model.Post
import com.app.dolt.model.PostRequest
import com.app.dolt.model.ProfileRequest
import com.app.dolt.model.SignUpRequest
import com.app.dolt.model.UserSimple
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Interfaz que define los endpoints de la API para la aplicación Dolt!.
 * Utiliza Retrofit para gestionar las peticiones HTTP.
 */
interface ApiService {

    /**
     * Registra un nuevo usuario.
     * 
     * @param SignUpRequest : Datos necesarios para el registro.
     * @return [Response] sin contenido si la operación es exitosa.
     */
    @POST("signup/")
    suspend fun signup(
        @Body signupRequest: SignUpRequest
    ): Response<Unit>

    /**
     * Inicia sesión con las credenciales proporcionadas.
     * 
     * @param loginRequest : Datos de inicio de sesión.
     * @return [Response] que contiene el token de autenticación y la información del usuario.
     */
    @POST("login/")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("google-login/")
    suspend fun googleLogin(
        @Body request: GoogleLoginRequest
    ): Response<LoginResponse>

    /**
     * Cierra la sesión del usuario.
     * 
     * @param logoutRequest : Datos necesarios para cerrar la sesión.
     * @return [Response] sin contenido si la operación es exitosa.
     */
    @POST("logout/")
    suspend fun logout(
        @Body logoutRequest: LogoutRequest
    ): Response<Unit>

    /**
     * Valida el token de sesión actual.
     * 
     * @return [Response] sin contenido si el token es válido.
     */
    @GET("check/") // Endpoint para validar el token
    suspend fun validateToken(): Response<Unit>

    /**
     * Obtiene la lista de desafíos disponibles.
     * 
     * @return Lista de objetos [Challenge].
     */
    @GET("challenges/")
    suspend fun getChallenges(): List<Challenge>

    /**
     * Obtiene el perfil de un usuario específico.
     * 
     * @param username : Nombre de usuario.
     * @return [Response] que contiene los datos del perfil del usuario.
     */
    @GET("profile/{username}")
    suspend fun getProfile(@Path("username") username: String): Response<UserProfile>

    /**
     * Actualiza el perfil del usuario en la API.
     *
     * @param profileRequest : Datos del perfil que se desea actualizar.
     * @return [Response] sin contenido si la operación es exitosa.
     */
    @POST("profile/")
    suspend fun updateProfile(
        @Body profileRequest: ProfileRequest
    ): Response<Unit>


    @DELETE("profile/")
    suspend fun deleteUser() : Response<Unit>


    @Multipart
    @POST("profile/image/")  // Usamos PATCH porque solo actualizamos un campo
    suspend fun updateProfileImage(
        @Part image: MultipartBody.Part
    ): Response<Unit>

    /**
     * Obtiene el feed de publicaciones.
     * 
     * @param size : Número de publicaciones a obtener.
     * @return Lista de objetos [Post].
     */    
    @GET("feed/{size}")
    suspend fun getFeed(@Path("size") size: Int): List<Post>

    /**
     * Obtiene la lista de usuarios seguidos.
     * 
     * @return [Response] que contiene la información de los usuarios seguidos.
     */
    @GET("follow/{username}")
    suspend fun getFollow(@Path("username") username: String) : FollowResponse

    /**
     * Envía una solicitud para seguir a un usuario.
     * 
     * @param followRequest : Datos del usuario a seguir.
     * @return [Response] sin contenido si la operación es exitosa.
     */
    @POST("follow/")
    suspend fun follow(
        @Body followRequest : FollowRequest
    ) : Response<Unit>

    /**
     * Envía una solicitud dejar de seguir a un usuario.
     * 
     * @param followRequest : Datos del usuario a dejar de seguir.
     * @return [Response] sin contenido si la operación es exitosa.
     */
    @POST("unfollow/")
    suspend fun unfollow(
        @Body followRequest : FollowRequest
    ) : Response<Unit>

    /**
     * Busca usuarios cuyos nombre coincida con el texto dado.
     * 
     * @param text : Texto de búsqueda.
     * @return Lista de objetos [UserSimple] encontrados.
     */
    @GET("search/{text}")
    suspend fun search(@Path("text") text: String): List<UserSimple>

    /**
     * Crea una nueva publicación en la API.
     *
     * @param postRequest : Datos de la publicación que se desea crear.
     * @return [Response] sin contenido si la operación es exitosa.
     */    
    @POST("post/")
    suspend fun post(
        @Body postRequest: PostRequest
    ): Response<Unit>


    // Método para obtener un post por ID (actualmente no utilizado).
    //@GET("posts/{id}")
    //suspend fun getPostById(@Path("id") id: Int): Challenge
}
