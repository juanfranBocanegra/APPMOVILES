package com.app.dolt.model

/**
 * Clase de datos utilizada para enviar una solicitud de seguimiento a un usuario.
 * 
 * @property username : Nombre de usuario al que se desea seguir o dejar de seguir.
 */
data class FollowRequest (
    val username : String
)