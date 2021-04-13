package com.focus.focusproductivitymanager

import android.app.Application
import com.focus.focusproductivitymanager.task.TaskRepository
import com.focus.focusproductivitymanager.task.TaskRoomDatabase
import com.focus.focusproductivitymanager.timer.TimerRepository
import com.focus.focusproductivitymanager.timer.TimerRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class FocusApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val task_database by lazy {
        TaskRoomDatabase.getDatabase(this, applicationScope)
    }
    val timer_database by lazy {
        TimerRoomDatabase.getDatabase(this, applicationScope)
    }
    val task_repository by lazy {
        TaskRepository(task_database.taskDao())
    }
    val timer_repository by lazy {
        TimerRepository(timer_database.timerDao())
    }
}
