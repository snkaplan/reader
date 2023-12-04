package com.sk.reader.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sk.reader.data.dto.bookdto.toBook
import com.sk.reader.data.repository.book.BookRepository
import com.sk.reader.data.repository.user.UserRepository
import com.sk.reader.model.MBook
import com.sk.reader.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository
) :
    ViewModel() {
    var uiState = MutableStateFlow(BookDetailsScreenState())
        private set
    var errorFlow = MutableSharedFlow<String>()
        private set

    fun getBook(id: String) {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true)
            val apiRequest = async { getBookFromApi(id) }
            val fireStore = async { getBookFromFirestore(id) }
            awaitAll(apiRequest, fireStore)
            uiState.value = uiState.value.copy(isLoading = false)

        }
    }

    private suspend fun getBookFromApi(id: String) {
        when (val result = bookRepository.getBook(id)) {
            is Resource.Error -> {
                uiState.value = uiState.value.copy(isLoading = false)
            }

            is Resource.Success -> {
                result.data?.toBook()?.let {
                    uiState.value = uiState.value.copy(book = it)
                } ?: kotlin.run {
                    onError("Error happened try later :/")
                }
            }
        }
    }

    private suspend fun getBookFromFirestore(id: String) {
        userRepository.getCurrentUser()?.uid?.let { safeId ->
            when (val result = bookRepository.getBookFromFirestoreByGoogleId(id, safeId)) {
                is Resource.Error -> {
                    uiState.value = uiState.value.copy(isLoading = false)
                }

                is Resource.Success -> {
                    result.data?.let {
                        uiState.value = uiState.value.copy(bookSaved = true)
                    }
                }
            }
        }
    }

    fun saveBook() {
        viewModelScope.launch {
            uiState.value.book?.let { safeBook ->
                uiState.value = uiState.value.copy(isLoading = true)
                val bookToSave = MBook(
                    title = safeBook.title,
                    authors = safeBook.authors,
                    description = safeBook.description,
                    thumbnail = safeBook.thumbnail,
                    smallThumbnail = safeBook.smallThumbnail,
                    publishedDate = safeBook.publishedDate,
                    categories = safeBook.categories,
                    pageCount = safeBook.pageCount,
                    userId = userRepository.getCurrentUser()?.uid,
                    googleBookId = safeBook.id
                )
                when (bookRepository.saveBook(bookToSave)) {
                    is Resource.Error -> {
                        uiState.value = uiState.value.copy(isLoading = false)
                    }

                    is Resource.Success -> {
                        uiState.value = uiState.value.copy(isLoading = false, bookSaved = true)
                    }
                }
            }
        }
    }

    private suspend fun onError(errorMessage: String) {
        uiState.value = uiState.value.copy(isLoading = false)
        errorFlow.emit(errorMessage)
    }
}