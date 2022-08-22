package com.ogurt.newsapp.use_cases

import com.ogurt.newsapp.model.ArticleDomainModel
import com.ogurt.newsapp.model.toNewsEntity
import com.ogurt.newsapp.repo.NewsRepository
import com.ogurt.newsapp.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddItemToBookmarksUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
) {

    suspend operator fun invoke(article: ArticleDomainModel): Flow<Resource<Unit>> = flow {
        newsRepository.addNewsToBookmark(article.toNewsEntity().copy(isSelected = true)).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    emit(Resource.Loading())
                }
                is Resource.Success -> {
                    emit(Resource.Success(Unit))
                }
                is Resource.Error -> {
                    emit(Resource.Error(result.message ?: 0))
                }
            }
        }
    }
}