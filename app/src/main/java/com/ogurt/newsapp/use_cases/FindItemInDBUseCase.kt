package com.ogurt.newsapp.use_cases

import com.ogurt.newsapp.model.ArticleDomainModel
import com.ogurt.newsapp.repo.NewsRepository
import javax.inject.Inject

class FindItemInDBUseCase @Inject constructor(
    private val repository: NewsRepository,
) {

    suspend operator fun invoke(item: ArticleDomainModel): Boolean {
        return repository.isBookmarks(item.publishedAt)
    }
}