package com.sk.reader.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sk.reader.data.datasource.UserRemoteDataSource
import com.sk.reader.data.datasource.UserRemoteDataSourceImpl
import com.sk.reader.data.repository.UserRepository
import com.sk.reader.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun providesAuthRepository(dataSource: UserRemoteDataSource): UserRepository {
        return UserRepositoryImpl(dataSource)
    }

    @Singleton
    @Provides
    fun providesAuthRemoteDataSour(
        auth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): UserRemoteDataSource {
        return UserRemoteDataSourceImpl(auth, firebaseFirestore)
    }
}