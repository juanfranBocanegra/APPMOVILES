package com.app.dolt.model

import com.app.dolt.api.RetrofitClient

data class Post (
    val id: String = "NULL",
    var user: String = "NULL",
    val text : String = "NULL",
    var date: String = "NULL",
    var challenge: String = "NULL",
    val challenge_es: String = "NULL",
    val name_user: String = "NULL",
    val profile_image : String = "NULL"
) {
    fun getProfileImageUrl(): String {

        return RetrofitClient.DOMAIN.removeSuffix("/") + profile_image
    }
}