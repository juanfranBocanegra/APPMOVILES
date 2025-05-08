package com.app.dolt.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.dolt.model.Challenge

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM Challenge")
    suspend fun getAllChallenges(): List<Challenge>

    @Query("SELECT * FROM Challenge WHERE id = :id LIMIT 1")
    suspend fun getChallengeById(id: String): Challenge?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertChallenges(challenges: List<Challenge>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertChallenge(challenge: Challenge)

    @Query("DELETE FROM Challenge")
    suspend fun deleteAllChallenges()
}