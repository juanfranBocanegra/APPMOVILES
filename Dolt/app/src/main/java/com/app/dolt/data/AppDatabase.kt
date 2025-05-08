package com.app.dolt.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.dolt.model.Challenge
import com.app.dolt.model.Post
import com.app.dolt.model.UserProfile
import com.app.dolt.model.UserStats

@Database(
    entities = [Post::class, Challenge::class, UserProfile::class, UserStats::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun challengeDao(): ChallengeDao
    abstract fun postDao(): PostDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun userStatsDao(): UserStatsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                "dolt_database"
                            ).fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}