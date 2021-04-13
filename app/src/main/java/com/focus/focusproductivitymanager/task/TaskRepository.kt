package com.focus.focusproductivitymanager.task

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

/**
 * Manages the data source for the task list.
 */
class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
    val countRows: Flow<Int> = taskDao.countRows()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(task: Task) {

        taskDao.insert(task)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

}
