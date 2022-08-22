package com.ogurt.newsapp.model

data class ArticleDomainModel(
    val isSelected: Boolean,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val source: NewsModel.Article.Source?,
    val title: String?,
    val url: String,
    val urlToImage: String?,
)
