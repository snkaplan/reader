package com.sk.reader.data.datasource.book

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.sk.reader.data.dto.bookdto.Item
import com.sk.reader.data.network.BooksApi
import com.sk.reader.utils.Resource
import com.sk.reader.utils.await
import com.sk.reader.utils.handleApi

private const val BOOKS_TABLE_NAME = "books"

class BookRemoteDataSourceImpl(
    private val booksApi: BooksApi,
    private val firebaseFirestore: FirebaseFirestore
) : BookRemoteDataSource {
    override suspend fun getBook(id: String): Resource<Item> {
        return handleApi {
            booksApi.getBookInfo(id)
        }
    }

    override suspend fun getBookFromFirestoreByGoogleId(
        id: String,
        userId: String
    ): Resource<Map<String, Any?>> {
        return try {
            val result = firebaseFirestore.collection(BOOKS_TABLE_NAME)
                .whereEqualTo("google_book_id", id).whereEqualTo("user_id", userId).get().await()
            if (result.documents.isEmpty().not()) {
                return Resource.Success(result.documents[0].data)
            }
            return Resource.Success(null)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.message ?: "General exception")
        }
    }

    override suspend fun getBookFromFirestoreById(
        id: String,
        userId: String
    ): Resource<DocumentSnapshot> {
        return try {
            val result = firebaseFirestore.collection(BOOKS_TABLE_NAME).document(id).get().await()
            if (result != null) {
                return Resource.Success(result)
            }
            return Resource.Success(null)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.message ?: "General exception")
        }
    }

    override suspend fun getUserBooks(userId: String): Resource<List<DocumentSnapshot>> {
        return try {
            val result =
                firebaseFirestore.collection(BOOKS_TABLE_NAME).whereEqualTo("user_id", userId).get()
                    .await()
            if (result.documents.isEmpty().not()) {
                return Resource.Success(result.documents)
            }
            return Resource.Success(null)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.message ?: "General exception")
        }

    }

    override suspend fun saveBook(book: Map<String, Any?>): Resource<Unit> {
        return try {
            val documentReference = firebaseFirestore.collection(BOOKS_TABLE_NAME).add(book).await()
            firebaseFirestore.collection(BOOKS_TABLE_NAME).document(documentReference.id)
                .update(hashMapOf("id" to documentReference.id) as Map<String, Any>).await()
            return Resource.Success(null)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.message ?: "General exception")
        }
    }

    override suspend fun updateBook(
        docId: String,
        updatedFields: Map<String, Any?>
    ): Resource<Unit> {
        return try {
            firebaseFirestore.collection(BOOKS_TABLE_NAME).document(docId).update(updatedFields)
                .await()
            return Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.message ?: "General exception")
        }
    }

    override suspend fun deleteBook(docId: String): Resource<Unit> {
        return try {
            firebaseFirestore.collection(BOOKS_TABLE_NAME).document(docId).delete()
                .await()
            return Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.message ?: "General exception")
        }
    }
}