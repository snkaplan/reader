package com.sk.reader.ui.screens.details

import com.sk.reader.model.Book

sealed class BookDetailsScreenState {
    object Loading : BookDetailsScreenState()
    object Idle : BookDetailsScreenState()
    data class Success(val data: Book) : BookDetailsScreenState()
    data class Error(val message: String) : BookDetailsScreenState()
}