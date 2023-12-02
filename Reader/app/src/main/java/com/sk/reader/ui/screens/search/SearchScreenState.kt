package com.sk.reader.ui.screens.search

import com.sk.reader.model.Book

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    object Idle : SearchScreenState()
    data class Success(val data: List<Book>) : SearchScreenState()
    data class Error(val message: String) : SearchScreenState()
}