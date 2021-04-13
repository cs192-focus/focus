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
import com.focus.focusproductivitymanager.timer.OnTimerDelete
import com.focus.focusproductivitymanager.timer.Timer
import com.focus.focusproductivitymanager.timer.TimerListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * The main Activity, the first activity that the user sees upon startup.
 */
class TimerListActivity : AppCompatActivity(), OnTimerDelete {

    private val newTimerActivityRequestCode = 2
    private var timerCount = 0
    private val timerViewModel: TimerViewModel by viewModels {
        TimerViewModelFactory((application as FocusApplication).timer_repository)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TimerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_list)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Hook the TimerListAdapter into the UI. The timers are displayed using a
        // RecyclerView - this is a list view that is less memory-intensive for
        // medium-sized lists.
        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        adapter = TimerListAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        timerViewModel.allTimers.observe(this, Observer {timers ->
            val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            adapter.submitList(timers)
            recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
        })
        timerViewModel.countRows.observe(this, Observer {count -> timerCount = count?:0})

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            onAddButtonSelected(view)
        }
    }

    /**
     * Called when the Delete button is pressed on a timer list item.
     */
    override fun timerDelete(timer: Timer) {
        timerViewModel.delete(timer)
    }

    private fun onAddButtonSelected(view: View) {
        val intent = Intent(this, AddTimerActivity::class.java)
        startActivityForResult(intent, newTimerActivityRequestCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newTimerActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringArrayListExtra("com.focus.focusproductivitymanager.REPLY")?.let { reply ->

                // Process the data passed in from the AddTimerActivity.
                val title = if (reply[0].isEmpty()) {
                    "Timer${timerCount + 1}" // Default item title.
                } else {
                    reply[0]
                }
                val notes = reply[1]
                val date = if (reply[2].isEmpty()) {
                    LocalDate.now()
                } else {
                    LocalDate.parse(reply[2], DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                }
                val time_start = if (reply[3].isEmpty()) {
                    LocalTime.of(LocalTime.now().hour + 1, 0)
                } else {
                    LocalTime.parse(reply[3])
                }
                val time_end = if (reply[4].isEmpty()) {
                    LocalTime.of(LocalTime.now().hour + 1, 30)
                } else {
                    LocalTime.parse(reply[4])
                }

                timerViewModel.insert(Timer(
                        0,
                        title,
                        date,
                        time_start,
                        time_end,
                        notes
                ))
            }
        }
    }

}