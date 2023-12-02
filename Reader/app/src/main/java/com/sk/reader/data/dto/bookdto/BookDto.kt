package com.sk.reader.data.dto.bookdto

import com.sk.reader.model.Book

data class BookDto(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)

fun BookDto.toBookList(): List<Book> {
    return items.map { bookItem ->
        Book(
            bookItem.id,
            bookItem.volumeInfo.title,
            bookItem.volumeInfo.authors?.joinToString(", ").orEmpty(),
            bookItem.volumeInfo.description.orEmpty(),
            bookItem.volumeInfo.imageLinks?.thumbnail
                ?: "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80",
            bookItem.volumeInfo.publishedDate.orEmpty(),
            bookItem.volumeInfo.categories?.joinToString(" - ").orEmpty()
        )
    }
}