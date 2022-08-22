package com.ogurt.newsapp.use_cases

import com.ogurt.newsapp.model.ArticleDomainModel
import com.ogurt.newsapp.model.toArticleDomainModel
import com.ogurt.newsapp.repo.NewsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchDatabaseUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
) {

    operator fun invoke(searchQuery: String): Flow<List<ArticleDomainModel>> {
        return newsRepository.searchDatabase(searchQuery)
            .map { it.map { newsEntity -> newsEntity.toArticleDomainModel() } }
    }
}