package com.focus.focusproductivitymanager

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * The data access object for the Task database.
 */
@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Insert
    suspend fun insert(task: Task)

    @Query("SELECT COUNT(id) FROM tasks")
    fun countRows(): Flow<Int>

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(task: Task)
}
