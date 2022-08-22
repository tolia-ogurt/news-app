package com.ogurt.newsapp.repo

import com.ogurt.newsapp.BuildConfig.API_KEY
import com.ogurt.newsapp.R
import com.ogurt.newsapp.model.ArticleDomainModel
import com.ogurt.newsapp.model.NewsModel
import com.ogurt.newsapp.model.toArticleDomainModel
import com.ogurt.newsapp.room.NewsDao
import com.ogurt.newsapp.room.NewsEntity
import com.ogurt.newsapp.service.NewsService
import com.ogurt.newsapp.utils.Resource
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class NewsRepositoryImpl @Inject constructor(
    private val newsService: NewsService,
    private val dao: NewsDao,
) : NewsRepository {

    override suspend fun getAllNews(): Flow<Resource<List<ArticleDomainModel>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(newsService.getAllNews().articles?.map { it.toArticleDomainModel() }))
        } catch (http: HttpException) {
            emit(Resource.Error(message = (R.string.exception)))
        } catch (io: IOException) {
            emit(Resource.Error(message = (R.string.error_internet_connection)))
        }
    }

    override suspend fun searchNews(searchQuery: String): Flow<Resource<List<NewsModel.Article>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(newsService.searchNews(
                        searchQuery.trim().lowercase(),
                        apiKey = API_KEY).articles)
                )
            } catch (http: HttpException) {
                emit(Resource.Error(message = (R.string.exception)))
            } catch (io: IOException) {
                emit(Resource.Error(message = (R.string.error_internet_connection)))
            }
        }

    override suspend fun addNewsToBookmark(newsEntity: NewsEntity): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(dao.addNewsToBookmark(newsEntity)))
        } catch (exception: Exception) {
            emit(Resource.Error(message = (R.string.exception)))
        }
    }

    override suspend fun getNewsFromBookmarkFromDB(): Flow<Resource<List<ArticleDomainModel>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(dao.getAllNewsFromBookmark()
                    .map { it.toArticleDomainModel() }))
            } catch (exception: Exception) {
                emit(Resource.Error(message = (R.string.exception)))
            }
        }

    override suspend fun deleteNewsFromBookmarks(publishedAt: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(dao.deleteNewsFromBookmarks(publishedAt)))
        } catch (exception: Exception) {
            emit(Resource.Error(message = (R.string.exception)))
        }
    }

    override suspend fun isBookmarks(publishedAt: String): Boolean {
        return dao.isBookmarks(publishedAt)
    }

    override fun searchDatabase(searchQuery: String): Flow<List<NewsEntity>> {
        return dao.searchDatabase(searchQuery)
    }
}