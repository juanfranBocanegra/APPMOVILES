package com.app.dolt.model

data class FollowResponse (
    val followers: List<Any> = listOf(),
    val following: List<Any> = listOf()
)