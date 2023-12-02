package com.sk.reader.data.datasource.search

import com.sk.reader.data.dto.bookdto.BookDto
import com.sk.reader.data.network.BooksApi
import com.sk.reader.utils.Resource
import com.sk.reader.utils.handleApi

class SearchRemoteDataSourceImpl(private val booksApi: BooksApi) : SearchRemoteDataSource {
    override suspend fun searchBooks(searchQuery: String): Resource<BookDto> {
        return handleApi {
            booksApi.searchBooks(searchQuery)
        }
    }
}