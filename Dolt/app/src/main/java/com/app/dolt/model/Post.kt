package com.app.dolt.model

data class Post (
    val id: String = "NULL",
    var user: String = "NULL",
    val text : String = "NULL",
    var date: String = "NULL",
    var challenge: String = "NULL",
    val challenge_es: String = "NULL",
    val name_user: String = "NULL"
)