package com.sk.reader.model

data class User(
    val id: String? = null,
    val uid: String?,
    val name: String,
    val lastName: String,
    val avatarUrl: String? = null,
    val quote: String? = null,
    val profession: String? = null
) {
    fun toFirebaseMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "user_id" to uid,
            "name" to name,
            "last_name" to lastName,
            "avatar_url" to avatarUrl,
            "quote" to quote,
            "profession" to profession,
        )
    }

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
