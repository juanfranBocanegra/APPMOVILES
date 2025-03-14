package com.app.dolt.model

data class FollowResponse (
    val followers: List<UserSimple> = listOf(),
    val following: List<UserSimple> = listOf()
)