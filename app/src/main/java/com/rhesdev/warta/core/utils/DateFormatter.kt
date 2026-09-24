package com.rhesdev.warta.core.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatter {

    private val indonesian = Locale.forLanguageTag("id-ID")
    private val jakartaZone = ZoneId.of("Asia/Jakarta")

    // "11 Desember 2024"
    private val cardFormat = DateTimeFormatter.ofPattern("d MMMM yyyy", indonesian)

    // "Rabu, 11 Des 2024 18:21" (+ " WIB" appended below; Asia/Jakarta has no DST)
    private val detailFormat = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy HH:mm", indonesian)

    fun formatCardDate(isoDate: String): String {
        val zoned = parse(isoDate) ?: return isoDate
        return cardFormat.format(zoned)
    }

    fun formatDetailDate(isoDate: String): String {
        val zoned = parse(isoDate) ?: return isoDate
        return "${detailFormat.format(zoned)} WIB"
    }

    // Returns null on blank/unrecognized input so callers fall back to the raw string.
    private fun parse(isoDate: String): ZonedDateTime? {
        if (isoDate.isBlank()) return null
        runCatching { return Instant.parse(isoDate).atZone(jakartaZone) }
        runCatching { return OffsetDateTime.parse(isoDate).atZoneSameInstant(jakartaZone) }
        runCatching { return ZonedDateTime.parse(isoDate).withZoneSameInstant(jakartaZone) }
        runCatching { return LocalDateTime.parse(isoDate).atZone(jakartaZone) }
        runCatching { return LocalDate.parse(isoDate).atStartOfDay(jakartaZone) }
        return null
    }
}
