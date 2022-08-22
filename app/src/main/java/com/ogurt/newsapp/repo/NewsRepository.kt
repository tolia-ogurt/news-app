package com.ogurt.newsapp.repo

import com.ogurt.newsapp.model.ArticleDomainModel
import com.ogurt.newsapp.model.NewsModel
import com.ogurt.newsapp.room.NewsEntity
import com.ogurt.newsapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getAllNews(): Flow<Resource<List<ArticleDomainModel>>>
    suspend fun searchNews(searchQuery: String): Flow<Resource<List<NewsModel.Article>>>
    suspend fun addNewsToBookmark(newsEntity: NewsEntity): Flow<Resource<Unit>>
    suspend fun deleteNewsFromBookmarks(publishedAt: String): Flow<Resource<Unit>>
    suspend fun getNewsFromBookmarkFromDB(): Flow<Resource<List<ArticleDomainModel>>>
    suspend fun isBookmarks(publishedAt: String): Boolean
    fun searchDatabase(searchQuery: String): Flow<List<NewsEntity>>
}