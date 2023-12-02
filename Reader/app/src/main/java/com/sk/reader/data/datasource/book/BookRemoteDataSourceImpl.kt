package com.sk.reader.data.datasource.book

import com.sk.reader.data.dto.bookdto.Item
import com.sk.reader.data.network.BooksApi
import com.sk.reader.utils.Resource
import com.sk.reader.utils.handleApi

class BookRemoteDataSourceImpl(private val booksApi: BooksApi) : BookRemoteDataSource {
    override suspend fun getBook(id: String): Resource<Item> {
        return handleApi {
            booksApi.getBookInfo(id)
        }
    }
}