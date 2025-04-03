package com.app.dolt.model

/**
 * Clase de datos que representa la respuesta de la API sobre los seguidores y seguidos de un usuario.
 * 
 * @property followers : Lista de los usuarios que siguen al usuario actual.
 * @property following : Lista de los usuarios que el usuario actual sigue.
 */
data class FollowResponse (
    val followers: List<UserSimple> = listOf(),
    val following: List<UserSimple> = listOf()
)