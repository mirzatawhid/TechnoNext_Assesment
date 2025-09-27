package com.technonext.ltd.assesment.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.technonext.ltd.assesment.viewmodel.PostViewModel
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(viewModel: PostViewModel = hiltViewModel(), onNavigateToFavorites: () -> Unit) {
    val posts by viewModel.posts.collectAsState()
    val isEndReached by viewModel.isEndReached.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Live search debounce
    LaunchedEffect(searchQuery) {
        delay(300)
        viewModel.searchPosts(searchQuery)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
            searchQuery = ""
        }
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Posts") },
                actions = {
                    IconButton(onClick = { onNavigateToFavorites() }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Go to Favorites"
                        )
                    }
                }
            )
        }
    ) { paddingValues -> // <-- Receive inner padding from Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // <-- Apply padding to avoid overlap with TopAppBar
                .pullRefresh(pullRefreshState)
                .pointerInput(Unit) {}
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // 🔍 Search field at the top
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search by title") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                            viewModel.searchPosts(searchQuery)
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 📜 Posts list
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(posts) { index, post ->

                        if (searchQuery.isEmpty() && index >= posts.lastIndex - 3 && !isEndReached) {
                            LaunchedEffect(Unit) { viewModel.loadNextPage() }
                        }

                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = post.title, style = MaterialTheme.typography.titleLarge)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = post.body, style = MaterialTheme.typography.bodyMedium)
                                }

                                IconButton(onClick = { viewModel.toggleFavorite(post) }) {
                                    if (post.isFavorite) {
                                        Icon(
                                            imageVector = Icons.Filled.Favorite,
                                            contentDescription = "Favorite",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Outlined.FavoriteBorder,
                                            contentDescription = "Not Favorite"
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (!isEndReached && searchQuery.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = androidx.compose.ui.Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(androidx.compose.ui.Alignment.TopCenter)
            )
        }
    }

}
