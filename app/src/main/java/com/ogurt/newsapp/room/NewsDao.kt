package com.ogurt.newsapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewsToBookmark(newsEntity: NewsEntity)

    @Query("SELECT * FROM news_bookmark")
    suspend fun getAllNewsFromBookmark(): List<NewsEntity>

    @Query("DELETE FROM news_bookmark WHERE publishedAt = :publishedAt")
    suspend fun deleteNewsFromBookmarks(publishedAt: String)

    @Query("SELECT EXISTS(SELECT * FROM news_bookmark WHERE publishedAt = :publishedAt)")
    suspend fun isBookmarks(publishedAt: String): Boolean

    @Query("SELECT * FROM news_bookmark WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<NewsEntity>>
}