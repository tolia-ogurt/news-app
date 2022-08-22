package com.ogurt.newsapp.presentation.bookmarks

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ogurt.newsapp.model.ArticleDomainModel
import com.ogurt.newsapp.use_cases.AddItemToBookmarksUseCase
import com.ogurt.newsapp.use_cases.DeleteItemToBookmarksUseCase
import com.ogurt.newsapp.use_cases.GetNewsBookmarkUseCase
import com.ogurt.newsapp.use_cases.SearchDatabaseUseCase
import com.ogurt.newsapp.utils.Resource
import com.ogurt.newsapp.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val searchDatabaseUseCase: SearchDatabaseUseCase,
    private val addItemToBookmarksUseCase: AddItemToBookmarksUseCase,
    private val deleteItemToBookmarksUseCase: DeleteItemToBookmarksUseCase,
    private val getNewsBookmarkUseCase: GetNewsBookmarkUseCase,
) : ViewModel() {

    private val _newsBookmark = MutableLiveData<List<ArticleDomainModel>>()
    val newsBookmark: LiveData<List<ArticleDomainModel>> get() = _newsBookmark
    val isLoading = MutableLiveData(false)
    val error = SingleLiveEvent<Int>()
    val deleteItemAction = SingleLiveEvent<ArticleDomainModel>()

    fun getNewsFromBookmark() {
        viewModelScope.launch {
            getNewsBookmarkUseCase().collect {
                when (it) {
                    is Resource.Loading -> {
                        isLoading.value = true
                    }
                    is Resource.Success -> {
                        it.data?.let { model ->
                            _newsBookmark.value = model
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

    fun searchDataBase(searchQuery: Editable?): LiveData<List<ArticleDomainModel>> {
        return searchDatabaseUseCase("%$searchQuery%").asLiveData()
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
                        it.data
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