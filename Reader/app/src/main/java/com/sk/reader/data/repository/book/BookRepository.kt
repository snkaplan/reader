package com.sk.reader.data.repository.book

import com.google.firebase.firestore.DocumentSnapshot
import com.sk.reader.data.dto.bookdto.Item
import com.sk.reader.model.MBook
import com.sk.reader.utils.Resource

interface BookRepository {
    suspend fun getBook(id: String): Resource<Item>
    suspend fun getBookFromFirestoreByGoogleId(
        id: String,
        userId: String
    ): Resource<Map<String, Any?>>

    suspend fun getBookFromFirestoreById(id: String, userId: String): Resource<DocumentSnapshot>
    suspend fun saveBook(book: MBook): Resource<Unit>
    suspend fun getUserBooks(userId: String): Resource<List<DocumentSnapshot>>
}