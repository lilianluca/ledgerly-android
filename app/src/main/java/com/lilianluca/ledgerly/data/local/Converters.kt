package com.lilianluca.ledgerly.data.local

import androidx.room.TypeConverter
import java.time.LocalDate

/**
 * Teaches Room how to store types SQLite doesn't know.
 *
 * Dates are stored as ISO-8601 text ("2026-11-17"): it sorts correctly as text,
 * works with SQLite date functions such as strftime() and is readable in the Database Inspector.
 */
class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let(LocalDate::parse)
}
