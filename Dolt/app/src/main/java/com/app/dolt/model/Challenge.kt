package com.app.dolt.model

/**
 * Clase de datos que respresenta un desafío en la aplicación.
 * 
 * @property id : Identificador único del desafío.
 * @property name : Nombre del desafío en inglés.
 * @property name_es : Nombre del desafío en español.
 * @property detail : Descripción detallada del desafío en inglés.
 * @property detail_es : Descripción detallada del desafío en español.
 * @property challenge_type : Tipo de desafío.
 * @property available : Indica si el desafío está disponible.
 */
data class Challenge(
    val id: String = "NULL",
    var name: String = "NULL",
    val name_es : String = "NULL",
    var detail: String = "NULL",
    val detail_es: String = "NULL",
    val challenge_type: String = "NULL",
    val available: Boolean
)