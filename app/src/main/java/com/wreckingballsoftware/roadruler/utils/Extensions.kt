package com.wreckingballsoftware.roadruler.utils

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun OffsetDateTime.asISO8601String(): String {
    val f = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxx")
    return this.format(f)
}
