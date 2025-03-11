package com.app.dolt.model

data class SignUpRequest (
    val username : String,
    val name : String,
    val password : String,
    val password2: String
)