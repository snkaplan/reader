package com.sk.reader.data.datasource.book

import com.sk.reader.data.dto.bookdto.Item
import com.sk.reader.utils.Resource

interface BookRemoteDataSource {
    suspend fun getBook(id: String): Resource<Item>
    suspend fun saveBook(book: Map<String, Any>): Resource<Unit>
}