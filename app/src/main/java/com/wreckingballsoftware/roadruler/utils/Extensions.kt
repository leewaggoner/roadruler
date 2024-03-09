package com.wreckingballsoftware.roadruler.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private const val MILES_CONVERSION = "0.000621371192237"
private const val KILOMETERS_CONVERSION = "0.001"
private const val SCALE = 1
private const val ISO8601_FORMAT = "uuuu-MM-dd'T'HH:mm:ssxx"

fun OffsetDateTime.asISO8601String(): String {
    val iso8601Formatter = DateTimeFormatter.ofPattern(ISO8601_FORMAT)
    return this.format(iso8601Formatter)
}

fun String.iso8601ToUIDateString(): String {
    val iso8601Formatter = DateTimeFormatter.ofPattern(ISO8601_FORMAT)
    val dateTime = OffsetDateTime.parse(this, iso8601Formatter)
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    return dateTime.format(dateFormatter)
}

fun String.iso8601ToUIDateTimeString(): String {
    val iso8601Formatter = DateTimeFormatter.ofPattern(ISO8601_FORMAT)
    val dateTime = OffsetDateTime.parse(this, iso8601Formatter)
    val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
    return dateTime.format(dateTimeFormatter)
}

fun BigDecimal.metersToMiles(): BigDecimal =
    (this * MILES_CONVERSION.toBigDecimal()).setScale(SCALE, RoundingMode.HALF_UP)

fun BigDecimal.metersToKilometers(): BigDecimal =
    (this * KILOMETERS_CONVERSION.toBigDecimal()).setScale(SCALE, RoundingMode.HALF_UP)
