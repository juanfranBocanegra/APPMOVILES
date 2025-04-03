package com.app.dolt.model

/**
 * Clase de datos que representa la respuesta de la API tras iniciar sesión.
 * Contiene los tokens de autenticación necesarios para las siguientes peticiones.
 * 
 * @property refresh : Token de actualización.
 * @property access : Token de acceso.
 */
data class LoginResponse(
    val refresh: String,
    val access: String
)