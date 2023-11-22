package com.sk.reader.data.repository

import com.google.firebase.auth.FirebaseUser
import com.sk.reader.data.datasource.UserRemoteDataSource
import com.sk.reader.model.User
import com.sk.reader.utils.ApiResult

class UserRepositoryImpl(private val userRemoteDataSource: UserRemoteDataSource) : UserRepository {
    override suspend fun registerUser(email: String, password: String): ApiResult<FirebaseUser> {
        return userRemoteDataSource.registerUser(email, password)
    }

    override suspend fun signIn(email: String, password: String): ApiResult<FirebaseUser> {
        return userRemoteDataSource.signIn(email, password)
    }

    override suspend fun createUser(user: User): ApiResult<Unit> {
        return userRemoteDataSource.createUser(user.toFirebaseMap())
    }

    override fun getCurrentUser(): FirebaseUser? {
        return userRemoteDataSource.currentUser
    }
}