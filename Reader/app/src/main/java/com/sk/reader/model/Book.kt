package com.sk.reader.model

data class Book(
    var id: String,
    var title: String,
    var authors: String,
    var notes: String,
    var photoURl: String?,
    var publishedDate: String,
    var categories: String
)
