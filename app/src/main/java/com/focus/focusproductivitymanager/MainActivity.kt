package com.focus.focusproductivitymanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<Button>(R.id.button).setOnClickListener { view ->
            onTaskButtonSelected(view)
        }
        findViewById<Button>(R.id.button2).setOnClickListener { view ->
            onTimerButtonSelected(view)
        }
    }

    private fun onTaskButtonSelected(view: View) {
        val intent = Intent(this, TaskListActivity::class.java)
        startActivity(intent)
    }

    private fun onTimerButtonSelected(view: View) {
        val intent = Intent(this, TimerListActivity::class.java)
        startActivity(intent)
    }

}