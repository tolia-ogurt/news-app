package com.ogurt.newsapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_bookmark")
data class NewsEntity(
    val isSelected: Boolean,
    val author: String?,
    val content: String?,
    val description: String?,
    @PrimaryKey val publishedAt: String,
    val source: Source?,
    val title: String?,
    val url: String,
    val urlToImage: String?
) {
    data class Source(
        val name: String?
    )
}