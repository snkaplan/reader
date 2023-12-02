package com.sk.reader.data.repository.book

import com.sk.reader.data.datasource.book.BookRemoteDataSource
import com.sk.reader.data.dto.bookdto.Item
import com.sk.reader.model.MBook
import com.sk.reader.model.toSaveableBookMap
import com.sk.reader.utils.Resource

class BookRepositoryImpl(private val bookRemoteDataSource: BookRemoteDataSource) : BookRepository {
    override suspend fun getBook(id: String): Resource<Item> {
        return bookRemoteDataSource.getBook(id)
    }

    override suspend fun saveBook(book: MBook): Resource<Unit> {
        return bookRemoteDataSource.saveBook(book.toSaveableBookMap())
    }
}
