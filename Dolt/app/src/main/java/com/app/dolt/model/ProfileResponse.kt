package com.app.dolt.model
/*"name": "newname",
    "username": "usuario1",
    "num_followers": 0,
    "num_followed": 1*/

data class ProfileResponse(
    val name : String,
    val username : String,
    val num_followers : Int,
    val num_followed : Int
)