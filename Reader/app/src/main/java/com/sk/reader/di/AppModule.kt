package com.sk.reader.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sk.reader.data.datasource.search.SearchRemoteDataSource
import com.sk.reader.data.datasource.search.SearchRemoteDataSourceImpl
import com.sk.reader.data.datasource.user.UserRemoteDataSource
import com.sk.reader.data.datasource.user.UserRemoteDataSourceImpl
import com.sk.reader.data.network.BooksApi
import com.sk.reader.data.repository.search.SearchRepository
import com.sk.reader.data.repository.search.SearchRepositoryImpl
import com.sk.reader.data.repository.user.UserRepository
import com.sk.reader.data.repository.user.UserRepositoryImpl
import com.sk.reader.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Singleton
    @Provides
    fun provideBookApi(): BooksApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build().create(
                BooksApi::class.java
            )
    }

    @Singleton
    @Provides
    fun provideSearchRepository(searchRemoteDataSource: SearchRemoteDataSource): SearchRepository {
        return SearchRepositoryImpl(searchRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideSearchRemoteDataSource(booksApi: BooksApi): SearchRemoteDataSource {
        return SearchRemoteDataSourceImpl(booksApi)
    }
}