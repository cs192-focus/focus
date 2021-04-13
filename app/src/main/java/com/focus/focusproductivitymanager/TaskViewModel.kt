package com.focus.focusproductivitymanager

import androidx.lifecycle.*
import com.focus.focusproductivitymanager.task.Task
import com.focus.focusproductivitymanager.task.TaskRepository
import kotlinx.coroutines.launch

/**
 * Interface between the task data and the app Activities.
 */
class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // The LiveData class allows the UI to dynamically change in response to
    // changes in the back-end data.
    val allTasks: LiveData<List<Task>> = repository.allTasks.asLiveData()
    val countRows: LiveData<Int> = repository.countRows.asLiveData()

    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
