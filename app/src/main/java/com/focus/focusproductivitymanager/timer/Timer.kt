package com.focus.focusproductivitymanager.timer

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

/**
 * A data class for representing timers.
 */
@Entity(tableName = "timers")
data class Timer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val date: LocalDate = LocalDate.now(),
    val timeStart: LocalTime = LocalTime.of(LocalTime.now().hour + 1, 0),
    val timeEnd: LocalTime = LocalTime.of(LocalTime.now().hour + 1, 30),
    val notes: String = ""
)
