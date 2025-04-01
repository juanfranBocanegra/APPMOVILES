package com.app.dolt.model

import com.app.dolt.api.RetrofitClient

/*"name": "newname",
    "username": "usuario1",
    "num_followers": 0,
    "num_followed": 1*/

data class UserProfile(
    var name : String = "NULL",
    val username : String = "NULL",
    var profile_image : String = "NULL",
    val num_followers : Int = 0,
    val num_followed : Int = 0,
    val following: Boolean = false,
    val follower: Boolean = false
) {
    fun getProfileImageUrl(): String {

        return RetrofitClient.DOMAIN.removeSuffix("/") + profile_image
    }
}