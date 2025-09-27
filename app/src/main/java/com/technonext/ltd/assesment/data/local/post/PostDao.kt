package com.technonext.ltd.assesment.data.local.post

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getPosts(limit: Int, offset: Int): List<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<Post>)

    @Update
    suspend fun updatePost(post: Post)

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun getPostCount(): Int

    @Query("SELECT MAX(id) FROM posts")
    suspend fun getMaxPostId(): Int?

    @Query("SELECT * FROM posts WHERE isFavorite = 1")
    fun getFavoritePosts(): Flow<List<Post>>


    @Query("SELECT * FROM posts WHERE title LIKE '%' || :query || '%' ORDER BY id")
    suspend fun searchPosts(query: String): List<Post>

}
