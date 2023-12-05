package com.sk.reader.ui.screens.update

import com.sk.reader.model.MBook

data class BookUpdateScreenState(
    val isLoading: Boolean = false,
    val mBook: MBook? = null
)

sealed class BookUpdateScreenEvents() {
    object BookDeletedSuccessfully : BookUpdateScreenEvents()
}