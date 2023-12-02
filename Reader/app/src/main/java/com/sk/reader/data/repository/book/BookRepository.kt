package com.sk.reader.data.repository.book

import com.sk.reader.data.dto.bookdto.Item
import com.sk.reader.utils.Resource

interface BookRepository {
    suspend fun getBook(id: String): Resource<Item>
}