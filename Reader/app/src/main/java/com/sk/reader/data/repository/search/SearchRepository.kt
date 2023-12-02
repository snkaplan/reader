package com.sk.reader.data.repository.search

import com.sk.reader.data.dto.bookdto.BookDto
import com.sk.reader.utils.Resource

interface SearchRepository {
    suspend fun searchBooks(searchQuery: String): Resource<BookDto>
}