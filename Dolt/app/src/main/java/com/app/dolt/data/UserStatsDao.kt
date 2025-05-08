package com.app.dolt.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.app.dolt.model.UserStats

@Dao
interface UserStatsDao {
    /**
     * Inserta o actualiza las estadísticas del usuario en la base de datos.
     *
     * @param userStats : Objeto [UserStats] que contiene las estadísticas del usuario.
     */
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertUserStats(userStats: UserStats)

    /**
     * Obtiene las estadísticas del usuario desde la base de datos.
     *
     * @return Objeto [UserStats] con las estadísticas del usuario.
     */
    @Query("SELECT * FROM UserStats LIMIT 1")
    suspend fun getUserStats(): UserStats?

    @Query("DELETE FROM UserStats")
    suspend fun deleteAllUserStats()
}