package com.focus.focusproductivitymanager.timer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * The data access object for the Timer database.
 */
@Dao
interface TimerDao {

    @Query("SELECT * FROM timers")
    fun getAllTimers(): Flow<List<Timer>>

    @Insert
    suspend fun insert(timer: Timer)

    @Query("SELECT COUNT(id) FROM timers")
    fun countRows(): Flow<Int>

    @Query("DELETE FROM timers")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(timer: Timer)
}
