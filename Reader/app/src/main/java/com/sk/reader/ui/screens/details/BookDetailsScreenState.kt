package com.sk.reader.ui.screens.details

import com.sk.reader.model.Book

data class BookDetailsScreenState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val bookSaved: Boolean = false
)