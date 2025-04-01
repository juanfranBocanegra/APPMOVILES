package com.app.dolt.model

data class ProfileRequest (
    var name: String = "",
    var password: List<String> = listOf<String>()
)