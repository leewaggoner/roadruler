package com.wreckingballsoftware.roadruler.utils

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private const val MILES_CONVERSION = 0.0006213712f
//private val MILES_CONVERSION = 0.000621371192237.toBigDecimal()
private const val KILOMETERS_CONVERSION = 0.001f
//private val KILOMETERS_CONVERSION = 0.001.toBigDecimal()

fun OffsetDateTime.asISO8601String(): String {
    val f = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxx")
    return this.format(f)
}

fun Float.metersToMiles() = this * MILES_CONVERSION

fun Float.metersToKilometers() = this * KILOMETERS_CONVERSION

fun Float.toPrecisionString(precision: Int) = String.format("%.${precision}f", this)
