package com.sk.reader.data.datasource.book

import com.sk.reader.data.dto.bookdto.Item
import com.sk.reader.utils.Resource

interface BookRemoteDataSource {
    suspend fun getBook(id: String): Resource<Item>
    suspend fun getBookFromFirestore(id: String, userId: String): Resource<Map<String, Any?>>
    suspend fun saveBook(book: Map<String, Any>): Resource<Unit>
}