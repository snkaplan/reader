package com.sk.reader.model

open class Book(
    open var id: String? = null,
    open var title: String,
    open var authors: String,
    open var description: String,
    open var thumbnail: String?,
    open var smallThumbnail: String?,
    open var publishedDate: String,
    open var categories: String,
    open var pageCount: Int
)
