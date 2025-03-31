package com.app.dolt.model

import com.app.dolt.api.RetrofitClient


data class UserSimple(
    val name : String = "NULL",
    val username : String = "NULL",
    val profile_image: String = "NULL"
) {
    fun getProfileImageUrl(): String {

        return RetrofitClient.DOMAIN.removeSuffix("/") + profile_image
    }
}