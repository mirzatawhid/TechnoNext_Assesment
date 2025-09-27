package com.technonext.ltd.assesment.data.repository

import com.technonext.ltd.assesment.data.local.post.Post
import com.technonext.ltd.assesment.data.local.post.PostDao
import com.technonext.ltd.assesment.data.remote.PostApi
import kotlinx.coroutines.flow.Flow
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

    suspend fun searchPosts(query: String): List<Post> {
        // Search in Room first
        val cachedResults = dao.searchPosts(query)
        if (cachedResults.isNotEmpty()) return cachedResults

        // If online, fetch remaining posts from API
        // Here you can implement paging or fetch all posts and filter
        val allPosts = mutableListOf<Post>()
        var offset = 0
        val limit = 20
        while (true) {
            val page = api.getPosts(offset, limit)
            if (page.isEmpty()) break
            dao.insertPosts(page)
            allPosts.addAll(page)
            offset += limit
        }
        return allPosts.filter { it.title.contains(query, ignoreCase = true) }
    }

    suspend fun toggleFavorite(post: Post) : Post {
        val updated = post.copy(isFavorite = !post.isFavorite)
        dao.updatePost(updated)
        return updated
    }

    fun getFavoritePosts(): Flow<List<Post>> = dao.getFavoritePosts()

}

