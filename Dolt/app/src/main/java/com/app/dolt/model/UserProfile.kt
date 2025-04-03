package com.app.dolt.model

import com.app.dolt.api.RetrofitClient

/*"name": "newname",
    "username": "usuario1",
    "num_followers": 0,
    "num_followed": 1*/

/**
 * Clase de datos que representa el perfil de un usuario.
 * 
 * @property name : Nombre visible del usuario.
 * @property username : Nombre del usuario.
 * @property profile_image : Ruta de la imagen de perfil del usuario.
 * @property num_followers : Número de seguidores del usuario.
 * @property num_followed : Número de usuarios a los que sigue.
 * @property following : Indica si el ususario actual sigue a este perfil.
 * @property follower : Indica si este perfil sigue al usuario actual.
 */
data class UserProfile(
    var name : String = "NULL",
    val username : String = "NULL",
    var profile_image : String = "NULL",
    val num_followers : Int = 0,
    val num_followed : Int = 0,
    val following: Boolean = false,
    val follower: Boolean = false
) {
    /**
     * Obtiene la URL completa de la imagen de perfil del usuario.
     * 
     * @return URL de la imagen de perfil.
     */
    fun getProfileImageUrl(): String {
        
        return RetrofitClient.DOMAIN.removeSuffix("/") + profile_image
    }
}