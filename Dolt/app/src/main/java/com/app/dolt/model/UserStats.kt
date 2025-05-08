package com.app.dolt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserStats (
   @PrimaryKey val coins : Int,
   val vote_times : Int
)