package com.ogurt.newsapp.use_cases

import com.ogurt.newsapp.model.ArticleDomainModel
import com.ogurt.newsapp.repo.NewsRepository
import com.ogurt.newsapp.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
) {

    suspend operator fun invoke(): Flow<Resource<List<ArticleDomainModel>>> = flow {
        newsRepository.getAllNews().collect { result ->
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