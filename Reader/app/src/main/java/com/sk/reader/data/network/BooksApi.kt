package com.sk.reader.data.network

import com.sk.reader.data.dto.bookdto.BookDto
import com.sk.reader.data.dto.bookdto.Item
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksApi {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): Response<BookDto>

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") id: String): Response<Item>
}