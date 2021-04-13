package com.focus.focusproductivitymanager

import androidx.lifecycle.*
import com.focus.focusproductivitymanager.timer.Timer
import com.focus.focusproductivitymanager.timer.TimerRepository
import kotlinx.coroutines.launch

/**
 * Interface between the timer data and the app Activities.
 */
class TimerViewModel(private val repository: TimerRepository) : ViewModel() {

    // The LiveData class allows the UI to dynamically change in response to
    // changes in the back-end data.
    val allTimers: LiveData<List<Timer>> = repository.allTimers.asLiveData()
    val countRows: LiveData<Int> = repository.countRows.asLiveData()

    fun insert(timer: Timer) = viewModelScope.launch {
        repository.insert(timer)
    }

    fun delete(timer: Timer) = viewModelScope.launch {
        repository.delete(timer)
    }
}

class TimerViewModelFactory(private val repository: TimerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
