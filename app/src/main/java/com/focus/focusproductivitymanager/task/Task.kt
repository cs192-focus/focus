package com.focus.focusproductivitymanager.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

/**
 * A data class for representing tasks.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val notes: String = "",
    val dueDate: LocalDate = LocalDate.now(),
    val dueTime: LocalTime = LocalTime.MIDNIGHT,
    val priority: Int = 4,
    val isComplete: Boolean = false
)
