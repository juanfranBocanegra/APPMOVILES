package com.app.dolt.model

import com.app.dolt.api.RetrofitClient

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Clase de datos que representa una publicación en la aplicación.
 * 
 * @property id : Identificador único de la publicación.
 * @property user : Nombre de usuario del autor de la publicación.
 * @property text : Texto o contenido de la publicación.
 * @property date : Fecha en la que se creó la publicación.
 * @property challenge : Nombre del desafío asociado a la publicación (en inglés).
 * @property challenge_es : Nombre del desafío asociado a la publicación (en español).
 * @property name_user : Nombre visible del autor de la publicación.
 * @property profile_image : Ruta de la imagen de perfil del autor.
 */
@Entity
data class Post (
    @PrimaryKey val id: String = "NULL",
    var user: String = "NULL",
    val text : String = "NULL",
    var date: String = "NULL",
    var challenge: String = "NULL",
    val challenge_es: String = "NULL",
    val name_user: String = "NULL",
    val profile_image : String = "NULL",
    val points : Int = 0,
) {
    /**
     * Obtiene la URL completa de la imagen de perfil del autor de la publicación.
     * 
     * @return URL de la imagen de perfil.
     */
    fun getProfileImageUrl(): String {

        return RetrofitClient.DOMAIN.removeSuffix("/") + profile_image
    }
}