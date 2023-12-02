package com.sk.reader.data.repository.user

import com.google.firebase.auth.FirebaseUser
import com.sk.reader.data.datasource.user.UserRemoteDataSource
import com.sk.reader.model.User
import com.sk.reader.utils.Resource

class UserRepositoryImpl(private val userRemoteDataSource: UserRemoteDataSource) : UserRepository {
    override suspend fun registerUser(email: String, password: String): Resource<FirebaseUser> {
        return userRemoteDataSource.registerUser(email, password)
    }

    override suspend fun signIn(email: String, password: String): Resource<FirebaseUser> {
        return userRemoteDataSource.signIn(email, password)
    }

    override suspend fun createUser(user: User): Resource<Unit> {
        return userRemoteDataSource.createUser(user.toFirebaseMap())
    }

    override suspend fun signOut(): Resource<Unit> {
        return userRemoteDataSource.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return userRemoteDataSource.currentUser
    }

    override suspend fun getUser(): Resource<Map<String, Any?>> {
        return userRemoteDataSource.getUser()
    }
}