package com.app.dolt.model

data class VoteResponse(
    val challenge: Challenge,
    val posts: List<Post>
)
