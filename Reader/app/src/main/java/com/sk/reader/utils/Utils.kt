package com.sk.reader.utils

import com.google.firebase.Timestamp
import java.text.DateFormat

fun Timestamp.formatDate(): String {
    return DateFormat.getDateInstance().format(this.toDate()).toString().split(",")[0] // March 12
}