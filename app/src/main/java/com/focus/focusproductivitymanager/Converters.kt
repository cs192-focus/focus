package com.focus.focusproductivitymanager

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

/**
 * Facilitates the conversion of database data from its representation in the database
 * (a string) and its representation in the program code (LocalDate or LocalTime).
 */
class Converters {
    @TypeConverter
    fun stringToLocalDate(str: String?): LocalDate? {
        return str?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun localDateToString(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun stringToLocalTime(str: String?): LocalTime? {
        return str?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    fun localTimeToString(time: LocalTime?): String? {
        return time?.toString()
    }
}
