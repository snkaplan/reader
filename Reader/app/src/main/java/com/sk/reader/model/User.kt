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
}
