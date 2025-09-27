package com.technonext.ltd.assesment.viewmodel

import androidx.lifecycle.viewModelScope
import com.technonext.ltd.assesment.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import com.technonext.ltd.assesment.data.local.post.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repo: PostRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts = _posts.asStateFlow()

    private val _isEndReached = MutableStateFlow(false)
    val isEndReached: StateFlow<Boolean> = _isEndReached

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    private var currentPage = 0
    private var isLoading = false

    init {
        loadNextPage()
    }

    // Lazy load next page
    fun loadNextPage(forceRefresh: Boolean = false) {
        if (isLoading || _isEndReached.value) return
        isLoading = true

        viewModelScope.launch {
            val newPosts = repo.getPosts(currentPage, forceRefresh)
            if (newPosts.size < 5) _isEndReached.value = true
            _posts.value = if (forceRefresh && currentPage == 0) newPosts else _posts.value + newPosts
            currentPage++
            isLoading = false
            _isRefreshing.value = false
        }
    }

    fun searchPosts(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                // Reset to normal lazy loading
                currentPage = 0
                _isEndReached.value = false
                _posts.value = emptyList()
                loadNextPage()
            } else {
                val results = repo.searchPosts(query)
                _posts.value = results
            }
        }
    }

    fun refresh() {
        currentPage = 0
        _posts.value = emptyList()
        _isEndReached.value = false
        _isRefreshing.value = true
        loadNextPage(forceRefresh = true)
    }


}


