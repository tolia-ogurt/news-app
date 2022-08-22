package com.ogurt.newsapp.presentation.news_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogurt.newsapp.model.ArticleDomainModel
import com.ogurt.newsapp.use_cases.AddItemToBookmarksUseCase
import com.ogurt.newsapp.use_cases.DeleteItemToBookmarksUseCase
import com.ogurt.newsapp.use_cases.FindItemInDBUseCase
import com.ogurt.newsapp.use_cases.GetNewsUseCase
import com.ogurt.newsapp.use_cases.SearchNewsUseCase
import com.ogurt.newsapp.utils.Resource
import com.ogurt.newsapp.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val findItemInDBUseCase: FindItemInDBUseCase,
    private val searchNewsUseCase: SearchNewsUseCase,
    private val addItemToBookmarksUseCase: AddItemToBookmarksUseCase,
    private val deleteItemToBookmarksUseCase: DeleteItemToBookmarksUseCase,
) : ViewModel() {

    private val _news = MutableLiveData<List<ArticleDomainModel>>()
    val news: LiveData<List<ArticleDomainModel>> get() = _news
    val isLoading = MutableLiveData(false)
    val error = SingleLiveEvent<Int>()
    private val deleteItemAction = SingleLiveEvent<ArticleDomainModel>()

    fun searchNews(searchQuery: String) {
        viewModelScope.launch {
            searchNewsUseCase(searchQuery).collect {
                when (it) {
                    is Resource.Loading -> {
                        isLoading.value = true
                    }
                    is Resource.Success -> {
                        it.data?.let { model ->
                            _news.value = model
                        }
                        isLoading.value = false
                    }
                    is Resource.Error -> {
                        error.value = it.message ?: 0
                        isLoading.value = false
                    }
                }
            }
        }
    }

    private suspend fun checkIsBookmark(items: List<ArticleDomainModel>): List<ArticleDomainModel> {
        return items.map { it.copy(isSelected = findItemInDBUseCase(it)) }
    }

    fun getNews() {
        viewModelScope.launch {
            getNewsUseCase().collect {
                when (it) {
                    is Resource.Loading -> {
                        isLoading.value = true
                    }
                    is Resource.Success -> {
                        it.data?.let { model ->
                            _news.value = checkIsBookmark(model)
                        }
                        isLoading.value = false
                    }
                    is Resource.Error -> {
                        error.value = it.message ?: 0
                        isLoading.value = false
                    }
                }
            }
        }
    }

    fun transferringBookmarks(news: ArticleDomainModel, isSelected: Boolean) {
        if (news.isSelected) {
            deleteFromBookmarks(news)
        } else {
            addToBookmarks(news.copy(isSelected = isSelected))
        }
    }

    private fun addToBookmarks(news: ArticleDomainModel) {
        viewModelScope.launch {
            addItemToBookmarksUseCase(news).collect {
                when (it) {
                    is Resource.Loading -> {
                        isLoading.value = true
                    }
                    is Resource.Success -> {
                        isLoading.value = false
                    }
                    is Resource.Error -> {
                        error.value = it.message ?: 0
                        isLoading.value = false
                    }
                }
            }
        }
    }

    private fun deleteFromBookmarks(news: ArticleDomainModel) {
        viewModelScope.launch {
            deleteItemToBookmarksUseCase(news.publishedAt).collect {
                when (it) {
                    is Resource.Loading -> {
                        isLoading.value = true
                    }
                    is Resource.Success -> {
                        deleteItemAction.value = news
                        isLoading.value = false
                    }
                    is Resource.Error -> {
                        error.value = it.message ?: 0
                        isLoading.value = false
                    }
                }
            }
        }
    }
}