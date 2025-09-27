package com.technonext.ltd.assesment.data.local.post

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey val id: Int,
    val title: String,
    val body: String,
    val isFavorite: Boolean = false // default false
)