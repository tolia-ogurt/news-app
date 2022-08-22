package com.ogurt.newsapp.model

import com.ogurt.newsapp.room.NewsEntity

fun NewsModel.Article.toArticleDomainModel() = ArticleDomainModel(
    isSelected = false,
    author = author,
    content = content,
    description = description,
    publishedAt = publishedAt,
    source = source,
    title = title,
    url = url,
    urlToImage = urlToImage
)

fun NewsModel.Article.Source.toSourceEntity() = NewsEntity.Source(
    name = name
)

fun ArticleDomainModel.toNewsEntity() = NewsEntity(
    isSelected = isSelected,
    author = author,
    content = content,
    description = description,
    publishedAt = publishedAt,
    source = source?.toSourceEntity(),
    title = title,
    url = url,
    urlToImage = urlToImage
)

fun NewsEntity.Source.toSource() = NewsModel.Article.Source(
    name = name,
    id = null
)

fun NewsEntity.toArticleDomainModel() = ArticleDomainModel(
    isSelected = isSelected,
    author = author,
    content = content,
    description = description,
    publishedAt = publishedAt,
    source = source?.toSource(),
    title = title,
    url = url,
    urlToImage = urlToImage
)