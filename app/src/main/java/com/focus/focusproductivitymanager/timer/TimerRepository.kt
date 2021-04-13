package com.focus.focusproductivitymanager.timer

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

/**
 * Manages the data source for the timer list.
 */
class TimerRepository(private val timerDao: TimerDao) {

    val allTimers: Flow<List<Timer>> = timerDao.getAllTimers()
    val countRows: Flow<Int> = timerDao.countRows()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(timer: Timer) {

        timerDao.insert(timer)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(timer: Timer) {
        timerDao.delete(timer)
    }

}
