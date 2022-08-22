package com.ogurt.newsapp.use_cases

import com.ogurt.newsapp.repo.NewsRepository
import com.ogurt.newsapp.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteItemToBookmarksUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
) {

    suspend operator fun invoke(publishedAt: String): Flow<Resource<Unit>> = flow {
        newsRepository.deleteNewsFromBookmarks(publishedAt).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    emit(Resource.Loading())
                }
                is Resource.Success -> {
                    emit(Resource.Success(result.data))
                }
                is Resource.Error -> {
                    emit(Resource.Error(result.message ?: 0))
                }
            }
        }
    }
}