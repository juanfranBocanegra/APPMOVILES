package com.app.dolt.model

data class Challenge(
    val id: String = "NULL",
    var name: String = "NULL",
    val name_es : String = "NULL",
    var detail: String = "NULL",
    val detail_es: String = "NULL",
    val challenge_type: String = "NULL",
    val available: Boolean
)