package com.app.dolt.model

/**
 * Clase de datos que representa una solicitud de registro de un nuevo usuario.
 * 
 * @property username : Nombre del usuario.
 * @property name : Nombre visible del usuario.
 * @property password : Contraseña del usuario.
 * @property password2 : Confirmación de la contraseña.
 */
data class SignUpRequest (
    val username : String,
    val name : String,
    val password : String,
    val password2: String
)