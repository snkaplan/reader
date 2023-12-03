package com.sk.reader.ui.screens.home

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
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    var uiState = MutableStateFlow<HomeScreenState>(HomeScreenState.Idle)
        private set

    fun getUserBooks() {
        viewModelScope.launch {
            userRepository.getCurrentUser()?.let { safeUser ->
                uiState.value = HomeScreenState.Loading
                when (val result = bookRepository.getUserBooks(safeUser.uid)) {
                    is Resource.Error -> {
                        uiState.value = HomeScreenState.Error(result.message.orEmpty())
                    }

                    is Resource.Success -> {
                        val bookList = result.data?.map { snapshot ->
                            snapshot.toObject(
                                MBook::class.java
                            )
                        } ?: emptyList()
                        uiState.value =
                            HomeScreenState.Success(bookList)
                    }
                }

            }
        }
    }
}