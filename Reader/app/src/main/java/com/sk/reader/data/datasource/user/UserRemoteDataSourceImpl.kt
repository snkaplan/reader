package com.sk.reader.data.datasource.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.sk.reader.utils.Resource
import com.sk.reader.utils.await

private const val USERS_TABLE_NAME = "users"

class UserRemoteDataSourceImpl(
    private val auth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : UserRemoteDataSource {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun registerUser(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            return Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.message ?: "General exception")
        }
    }

    override suspend fun signIn(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.message ?: "General Exception")
        }
    }

    override suspend fun createUser(user: MutableMap<String, Any?>): Resource<Unit> {
        return try {
            firebaseFirestore.collection(USERS_TABLE_NAME).add(user).await()
            return Resource.Success(null)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.message ?: "General exception")
        }
    }

    override suspend fun getUser(): Resource<Map<String, Any?>> {
        return try {
            val result = firebaseFirestore.collection(USERS_TABLE_NAME)
                .whereEqualTo("user_id", currentUser?.uid).get().await()
            if (result.documents.isEmpty().not()) {
                return Resource.Success(result.documents[0].data)
            }
            return Resource.Success(null)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.message ?: "General exception")
        }
    }

    override suspend fun signOut(): Resource<Unit> {
        return try {
            auth.signOut()
            return Resource.Success(null)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.message ?: "General exception")
        }
    }
}