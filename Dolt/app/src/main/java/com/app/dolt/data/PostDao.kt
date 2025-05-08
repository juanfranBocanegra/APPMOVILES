package com.app.dolt.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.dolt.model.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM Post ORDER BY date DESC")
    suspend fun getAllPosts(): List<Post>

    @Query("SELECT * FROM Post WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: String): Post?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertPosts(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertPost(post: Post)

    @Query("DELETE FROM Post")
    suspend fun deleteAllPosts()
}