package com.sk.reader.data.dto.bookdto

import com.sk.reader.model.Book

data class BookDto(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)

fun BookDto.toBookList(): List<Book> {
    return items.map { bookItem ->
        bookItem.toBook()
    }
}

fun Item.toBook(): Book {
    return Book(
        this.id,
        this.volumeInfo.title,
        this.volumeInfo.authors?.joinToString(", ").orEmpty(),
        this.volumeInfo.description.orEmpty(),
        this.volumeInfo.imageLinks?.thumbnail,
        this.volumeInfo.imageLinks?.smallThumbnail,
        this.volumeInfo.publishedDate.orEmpty(),
        this.volumeInfo.categories?.joinToString(" - ").orEmpty(),
        this.volumeInfo.pageCount
    )
}