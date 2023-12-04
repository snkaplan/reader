package com.sk.reader.ui.screens.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sk.reader.data.repository.book.BookRepository
import com.sk.reader.data.repository.user.UserRepository
import com.sk.reader.model.MBook
import com.sk.reader.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookUpdateViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository
) :
    ViewModel() {
    var uiState = MutableStateFlow(BookUpdateScreenState())
        private set
    var errorFlow = MutableSharedFlow<String>()
        private set

    fun getBookFromFirestoreById(id: String) {
        viewModelScope.launch {
            userRepository.getCurrentUser()?.uid?.let { safeId ->
                uiState.value = uiState.value.copy(isLoading = true)
                when (val result = bookRepository.getBookFromFirestoreById(id, safeId)) {
                    is Resource.Error -> {
                        onError(result.message)
                    }

                    is Resource.Success -> {
                        result.data?.let {
                            uiState.value =
                                uiState.value.copy(
                                    mBook = it.toObject(MBook::class.java),
                                    isLoading = false
                                )
                        }
                    }
                }
            }
        }
    }

    private suspend fun onError(errorMessage: String?) {
        uiState.value = uiState.value.copy(isLoading = false)
        errorMessage?.let {
            errorFlow.emit(it)
        }
    }
}