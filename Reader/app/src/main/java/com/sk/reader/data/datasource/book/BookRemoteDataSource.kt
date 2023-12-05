package com.sk.reader.data.datasource.book

import com.google.firebase.firestore.DocumentSnapshot
import com.sk.reader.data.dto.bookdto.Item
import com.sk.reader.utils.Resource

interface BookRemoteDataSource {
    suspend fun getBook(id: String): Resource<Item>
    suspend fun getBookFromFirestoreByGoogleId(
        id: String,
        userId: String
    ): Resource<Map<String, Any?>>

    suspend fun getBookFromFirestoreById(id: String, userId: String): Resource<DocumentSnapshot>
    suspend fun getUserBooks(userId: String): Resource<List<DocumentSnapshot>>
    suspend fun saveBook(book: Map<String, Any?>): Resource<Unit>
    suspend fun updateBook(docId: String, updatedFields: Map<String, Any?>): Resource<Unit>
    suspend fun deleteBook(docId: String): Resource<Unit>
}