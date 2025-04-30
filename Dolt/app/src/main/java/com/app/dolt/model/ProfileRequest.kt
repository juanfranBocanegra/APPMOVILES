package com.app.dolt.model

import okhttp3.MultipartBody

/**
 * Clase de datos que representa una solicitud de un perfil de usuario.
 * 
 * @property name : Nombre de usuario.
 * @property password : Contraseña del usuario.
 */
data class ProfileRequest (
    var name: String = "",
    var password: List<String> = listOf<String>()
)