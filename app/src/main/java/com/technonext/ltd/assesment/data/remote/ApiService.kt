package com.technonext.ltd.assesment.data.remote

import com.technonext.ltd.assesment.data.local.post.Post
import retrofit2.http.GET
import retrofit2.http.Query


interface PostApi {
    @GET("posts")
    suspend fun getPosts(
        @Query("_start") start: Int,
        @Query("_limit") limit: Int
    ): List<Post>
}
