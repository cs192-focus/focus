package com.focus.focusproductivitymanager

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

/**
 * An Activity that is called when the user wants to add a new task.
 */
class AddTaskActivity : AppCompatActivity() {
    private lateinit var addTaskTitle: EditText
    private lateinit var addTaskTime: EditText
    private lateinit var addTaskNotes: EditText
    private lateinit var addTaskDate: EditText
    private lateinit var addTaskPriority1: RadioButton
    private lateinit var addTaskPriority2: RadioButton
    private lateinit var addTaskPriority3: RadioButton
    private lateinit var addTaskPriority4: RadioButton
    var cal = Calendar.getInstance()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        addTaskTitle = findViewById(R.id.addTaskTitle)
        addTaskNotes = findViewById(R.id.addTaskNotes)
        addTaskTime = findViewById(R.id.addTaskTime)
        addTaskDate = findViewById(R.id.addTaskDate)
        addTaskPriority1 = findViewById(R.id.addTaskPriority1)
        addTaskPriority2 = findViewById(R.id.addTaskPriority2)
        addTaskPriority3 = findViewById(R.id.addTaskPriority3)
        addTaskPriority4 = findViewById(R.id.addTaskPriority4)

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
                this@AddTaskActivity,
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
                this@AddTaskActivity,
                timeSetListener,
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
            val time = addTaskTime.text.toString()
            val date = addTaskDate.text.toString()
            val priority = when {
                addTaskPriority1.isChecked -> "1"
                addTaskPriority2.isChecked -> "2"
                addTaskPriority3.isChecked -> "3"
                addTaskPriority4.isChecked -> "4"
                else -> "4"
            }

            // Put everything in a List and pass it off to the main Activity, which will
            // faciliate the actual adding of the task.
            val task_args = arrayListOf(title, notes, time, date, priority)

            replyIntent.putExtra("com.focus.focusproductivitymanager.REPLY", task_args)
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }

    }
}