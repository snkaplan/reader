package com.sk.reader.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sk.reader.data.dto.bookdto.toBookList
import com.sk.reader.data.repository.search.SearchRepository
import com.sk.reader.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository) : ViewModel() {
    var searchState = MutableStateFlow<SearchScreenState>(SearchScreenState.Idle)
        private set

    fun searchBooks(searchQuery: String) {
        viewModelScope.launch {
            searchState.value = SearchScreenState.Loading
            when (val result = searchRepository.searchBooks(searchQuery)) {
                is Resource.Error -> {
                    searchState.value = SearchScreenState.Error(result.message.orEmpty())
                }

                is Resource.Success -> {
                    searchState.value = SearchScreenState.Success(result.data?.toBookList().orEmpty())
                }
            }
        }
    }
}