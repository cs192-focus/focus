package com.focus.focusproductivitymanager

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

/**
 * An Activity that is called when the user wants to add a new task.
 */
class AddTimerActivity : AppCompatActivity() {
    private lateinit var addTaskTitle: EditText
    private lateinit var addTaskTime: EditText
    private lateinit var addTaskTime2: EditText
    private lateinit var addTaskNotes: EditText
    private lateinit var addTaskDate: EditText
    var cal = Calendar.getInstance()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_timer)
        addTaskTitle = findViewById(R.id.addTaskTitle)
        addTaskNotes = findViewById(R.id.addTaskNotes)
        addTaskTime = findViewById(R.id.addTaskTime)
        addTaskTime2 = findViewById(R.id.addTaskTime2)
        addTaskDate = findViewById(R.id.addTaskDate)

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "MM/dd/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                addTaskDate.setText(sdf.format(cal.time))
            }

        addTaskDate.setOnClickListener {
            DatePickerDialog(
                this@AddTimerActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                cal.set(Calendar.HOUR, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                val myFormat = "hh:mm" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                addTaskTime.setText(sdf.format(cal.time))
            }

        addTaskTime.setOnClickListener {
            TimePickerDialog(
                this@AddTimerActivity,
                timeSetListener,
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        val timeSetListener2 =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                cal.set(Calendar.HOUR, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                val myFormat = "hh:mm" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                addTaskTime2.setText(sdf.format(cal.time))
            }

        addTaskTime2.setOnClickListener {
            TimePickerDialog(
                this@AddTimerActivity,
                timeSetListener2,
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        val button = findViewById<FloatingActionButton>(R.id.fabSave)
        button.setOnClickListener {

            val replyIntent = Intent()

            // Fetch data from the UI.
            val title = addTaskTitle.text.toString()
            val notes = addTaskNotes.text.toString()
            val time_start = addTaskTime.text.toString()
            val date = addTaskDate.text.toString()
            val time_end = addTaskTime2.text.toString()

            // Put everything in a List and pass it off to the main Activity, which will
            // faciliate the actual adding of the task.
            val task_args = arrayListOf(title, notes, date, time_start, time_end)

            replyIntent.putExtra("com.focus.focusproductivitymanager.REPLY", task_args)
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }

    }
}