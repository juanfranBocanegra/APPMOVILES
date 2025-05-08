package com.app.dolt.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.app.dolt.model.UserProfile

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM UserProfile")
    suspend fun getAllUserProfiles(): List<UserProfile>

    @Query("SELECT * FROM UserProfile WHERE username = :username")
    suspend fun getUserProfile(username: String): UserProfile?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertUserProfile(user: UserProfile)

    @Upsert
    suspend fun upsertUserProfile(profile: UserProfile)

    @Query("DELETE FROM UserProfile")
    suspend fun deleteAllUserProfiles()
}