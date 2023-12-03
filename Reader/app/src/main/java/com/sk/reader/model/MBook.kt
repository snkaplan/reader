package com.sk.reader.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class MBook(
    @Exclude override var id: String? = null,
    override var title: String,
    override var authors: String,
    override var description: String,
    override var thumbnail: String?,
    @get:PropertyName("small_thumbnail")
    @set:PropertyName("small_thumbnail")
    override var smallThumbnail: String?,
    @get:PropertyName("published_date")
    @set:PropertyName("published_date")
    override var publishedDate: String,
    override var categories: String,
    @get:PropertyName("page_count")
    @set:PropertyName("page_count")
    override var pageCount: Int,
    var notes: String? = null,
    var rating: Double? = null,
    @get:PropertyName("started_reading_at")
    @set:PropertyName("started_reading_at")
    var startedReading: Timestamp? = null,
    @get:PropertyName("finished_reading_at")
    @set:PropertyName("finished_reading_at")
    var finishedReading: Timestamp? = null,
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,
    @get:PropertyName("google_book_id")
    @set:PropertyName("google_book_id")
    var googleBookId: String? = null
) : Book(
    id,
    title,
    authors,
    description,
    thumbnail,
    smallThumbnail,
    publishedDate,
    categories,
    pageCount
) {
    companion object {
        fun fromFirebaseMap(map: Map<String, Any?>): User {
            return User(
                uid = map["user_id"].toString(),
                name = map["name"].toString(),
                lastName = map["last_name"].toString(),
                avatarUrl = map["avatar_url"].toString(),
                quote = map["quote"].toString(),
                profession = map["profession"].toString()
            )
        }
    }
}

fun MBook.toSaveableBookMap(): Map<String, Any> {
    val bookMap = mutableMapOf<String, Any>()
    bookMap["title"] = title
    bookMap["authors"] = authors
    bookMap["description"] = description
    bookMap["notes"] = ""
    bookMap["categories"] = categories
    bookMap["page_count"] = pageCount.toString()
    bookMap["thumbnail"] = thumbnail.toString()
    bookMap["small_thumbnail"] = smallThumbnail.toString()
    bookMap["published_date"] = publishedDate
    bookMap["rating"] = 0.0
    bookMap["google_book_id"] = googleBookId.toString()
    bookMap["user_id"] = userId.toString()
    return bookMap
}
