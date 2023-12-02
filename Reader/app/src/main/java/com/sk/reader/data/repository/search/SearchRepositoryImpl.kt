package com.sk.reader.data.repository.search

import com.sk.reader.data.datasource.search.SearchRemoteDataSource
import com.sk.reader.data.dto.bookdto.BookDto
import com.sk.reader.utils.Resource

class SearchRepositoryImpl(private val searchRemoteDataSource: SearchRemoteDataSource) : SearchRepository {
    override suspend fun searchBooks(searchQuery: String): Resource<BookDto> {
        return searchRemoteDataSource.searchBooks(searchQuery)
    }
}