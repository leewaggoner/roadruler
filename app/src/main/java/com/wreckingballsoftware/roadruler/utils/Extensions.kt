package com.wreckingballsoftware.roadruler.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private val MILES_CONVERSION = "0.000621371192237".toBigDecimal()
private val KILOMETERS_CONVERSION = "0.001".toBigDecimal()
private const val SCALE = 1

fun OffsetDateTime.asISO8601String(): String {
    val f = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxx")
    return this.format(f)
}

fun BigDecimal.metersToMiles(): BigDecimal =
    (this * MILES_CONVERSION).setScale(SCALE, RoundingMode.HALF_UP)

fun BigDecimal.metersToKilometers(): BigDecimal =
    (this * KILOMETERS_CONVERSION).setScale(SCALE, RoundingMode.HALF_UP)
