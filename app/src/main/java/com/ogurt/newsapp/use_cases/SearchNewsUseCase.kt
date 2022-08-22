package com.ogurt.newsapp.use_cases

import com.ogurt.newsapp.model.ArticleDomainModel
import com.ogurt.newsapp.model.toArticleDomainModel
import com.ogurt.newsapp.repo.NewsRepository
import com.ogurt.newsapp.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
) {

    suspend operator fun invoke(searchQuery: String): Flow<Resource<List<ArticleDomainModel>>> = flow {
            newsRepository.searchNews(searchQuery).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        emit(Resource.Loading())
                    }
                    is Resource.Success -> {
                        emit(Resource.Success(result.data?.map { it.toArticleDomainModel() }))
                    }
                    is Resource.Error -> {
                        emit(Resource.Error(result.message ?: 0))
                    }
                }
            }
        }
}