package com.app.dolt.model

/**
 * Clase de datos que representa una solicitud para cerrar sesión.
 * 
 * @property refresh : Token de actualización que se desea invalidar.
 */
data class LogoutRequest(
    val refresh: String,
)
