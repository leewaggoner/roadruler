package com.wreckingballsoftware.roadruler.utils

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private const val MILES_CONVERSION = 0.0006213712f
private const val KILOMETERS_CONVERSION = 0.001f

fun OffsetDateTime.asISO8601String(): String {
    val f = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxx")
    return this.format(f)
}

fun Float.metersToMiles() = this * MILES_CONVERSION

fun Float.metersToKilometers() = this * KILOMETERS_CONVERSION

fun Float.toPrecisionString(precision: Int) = String.format("%.${precision}f", this)
