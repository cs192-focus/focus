package com.focus.focusproductivitymanager.task

import com.focus.focusproductivitymanager.task.Task

interface OnTaskDelete {
    fun taskDelete(task: Task)
}