package com.app.dolt.model
/*"name": "newname",
    "username": "usuario1",
    "num_followers": 0,
    "num_followed": 1*/

data class UserProfile(
    val name : String = "NULL",
    val username : String = "NULL",
    val num_followers : Int = 0,
    val num_followed : Int = 0,
    val following: Boolean = false,
    val follower: Boolean = false
)