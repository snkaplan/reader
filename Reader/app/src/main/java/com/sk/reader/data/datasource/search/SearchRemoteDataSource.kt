package com.sk.reader.data.datasource.search

import com.sk.reader.data.dto.bookdto.BookDto
import com.sk.reader.utils.Resource

interface SearchRemoteDataSource {
    suspend fun searchBooks(searchQuery: String): Resource<BookDto>
}