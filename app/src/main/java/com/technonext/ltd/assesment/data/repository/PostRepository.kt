package com.technonext.ltd.assesment.data.repository

import com.technonext.ltd.assesment.data.local.post.Post
import com.technonext.ltd.assesment.data.local.post.PostDao
import com.technonext.ltd.assesment.data.remote.PostApi
import javax.inject.Inject


class PostRepository @Inject constructor(
    private val api: PostApi,
    private val dao: PostDao
) {
    private val pageSize = 5

    suspend fun getPosts(page: Int, forceRefresh: Boolean = false): List<Post> {
        val offset = page * pageSize
        val cached = dao.getPosts(pageSize, offset)

        return if (cached.isNotEmpty() && !forceRefresh) {
            cached
        } else {
            val remote = api.getPosts(start = offset, limit = pageSize)
            dao.insertPosts(remote) // REPLACE if conflict
            remote
        }
    }

    suspend fun getAllCachedPosts(): List<Post> = dao.getPosts(Int.MAX_VALUE, 0)
}

