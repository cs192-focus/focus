package com.focus.focusproductivitymanager.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * A data class for representing tasks.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val notes: String = "",
    val dueDate: LocalDate = LocalDate.parse("20210326", DateTimeFormatter.BASIC_ISO_DATE),
    val dueTime: LocalTime = LocalTime.parse("00:00:00"),
    val priority: Int = 4,
    val isComplete: Boolean = false
)
