package com.sk.reader.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sk.reader.data.dto.bookdto.toBook
import com.sk.reader.data.repository.book.BookRepository
import com.sk.reader.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {
    var bookState = MutableStateFlow<BookDetailsScreenState>(BookDetailsScreenState.Idle)
        private set

    fun getBook(id: String) {
        viewModelScope.launch {
            when (val result = bookRepository.getBook(id)) {
                is Resource.Error -> {
                    bookState.value = BookDetailsScreenState.Error(result.message.orEmpty())
                }

                is Resource.Success -> {
                    result.data?.toBook()?.let {
                        bookState.value =
                            BookDetailsScreenState.Success(it)
                    } ?: kotlin.run {
                        bookState.value =
                            BookDetailsScreenState.Error("Error happened try later :/")
                    }
                }
            }
        }
    }
}