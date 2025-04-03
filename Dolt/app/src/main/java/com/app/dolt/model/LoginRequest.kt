package com.app.dolt.model

/**
 * Clase de datos que representa una solicitud de inicio de sesión.
 * 
 * @property username : Nombre de usuario.
 * @property password : Contraseña del usuario.
 */
data class LoginRequest(
    val username: String,
    val password: String
)
