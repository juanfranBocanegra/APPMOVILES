package com.app.dolt.model

/**
 * Clase de datos que representa una solicitud para crear una nueva publicación.
 * 
 * @property challenge : Identificador del desafío asociado a la publicación.
 * @property text : Texto o contenido de la publicación.
 */
data class PostRequest (
    val challenge: String,
    val text: String,
)