package com.ogurt.newsapp.service

import com.ogurt.newsapp.BuildConfig.API_KEY
import com.ogurt.newsapp.model.NewsModel
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("everything?q=tesla&apiKey=$API_KEY")
    suspend fun getAllNews(): NewsModel

    @GET("everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("apiKey") apiKey: String,
    ): NewsModel
}