package com.focus.focusproductivitymanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * The main Activity, the first activity that the user sees upon startup.
 */
class MainActivity : AppCompatActivity(), OnTaskDelete {

    private val newTaskActivityRequestCode = 1
    private var taskCount = 0
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TaskApplication).repository)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Hook the TaskListAdapter into the UI. The tasks are displayed using a
        // RecyclerView - this is a list view that is less memory-intensive for
        // medium-sized lists.
        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        adapter = TaskListAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskViewModel.allTasks.observe(this, Observer {tasks ->
            val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            adapter.submitList(tasks)
            recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
        })
        taskViewModel.countRows.observe(this, Observer {count -> taskCount = count?:0})

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            onAddButtonSelected(view)
        }
    }

    /**
     * Called when the Delete button is pressed on a task list item.
     */
    override fun taskDelete(task: Task) {
        taskViewModel.delete(task)
    }

    private fun onAddButtonSelected(view: View) {
        val intent = Intent(this, AddTaskActivity::class.java)
        startActivityForResult(intent, newTaskActivityRequestCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newTaskActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringArrayListExtra("com.focus.focusproductivitymanager.REPLY")?.let { reply ->

                // Process the data passed in from the AddTaskActivity.
                val title = if (reply[0].isEmpty()) {
                    "New_Task_${taskCount + 1}" // Default item title.
                } else {
                    reply[0]
                }
                val notes = reply[1]
                val time = if (reply[2].isEmpty()) {
                    LocalTime.parse("00:00") // Default time.
                } else {
                    LocalTime.parse(reply[2])
                }
                val date = if (reply[3].isEmpty()) {
                    LocalDate.now()
                } else {
                    LocalDate.parse(reply[3], DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                }
                val priority = Integer.parseInt(reply[4])

                taskViewModel.insert(Task(
                    0,
                    title,
                    notes,
                    date,
                    time,
                    priority,
                    false))
            }
        }
    }

}