package com.app.dolt.model

import com.app.dolt.api.RetrofitClient

/**
 * Clase de datos que representa la información básica de un usuario.
 * 
 * @property name : Nombre visible del usuario.
 * @property username : Nombre del usuario.
 * @property profile_image : Ruta de la imagen de perfil del usuario.
 */
data class UserSimple(
    val name : String = "NULL",
    val username : String = "NULL",
    val profile_image: String = "NULL"
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