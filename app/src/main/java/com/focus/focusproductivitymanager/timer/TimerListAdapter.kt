package com.focus.focusproductivitymanager.timer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focus.focusproductivitymanager.R
import java.time.LocalDate
import java.time.LocalTime

/**
 * The display class for the timer list. Adapts the timer list data so it can be displayed in the UI.
 */
class TimerListAdapter(context: Context, listener: OnTimerDelete): ListAdapter<Timer, TimerListAdapter.TimerViewHolder>(TimerViewHolder.TimerComparator()) {

    // Enables the Activity to respond when something is clicked on the list itself
    // (in this case, when the Delete button is pressed)
    val mListener: OnTimerDelete = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        return TimerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        // When the UI is initialized with the timer list data, fill each item in the list with
        // the needed information. The TimerViewHolder class facilitates the insertion of
        // data and interactivity into the UI.
        val current = getItem(position)
        holder.bindTo(current.name, current.date, current.timeStart, current.timeEnd, current.notes)
        // Links the delete button to the delete functionality.
        holder.timerItemDeleteButton.setOnClickListener { onDelete(position) }
    }

    class TimerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timerItemView: TextView = itemView.findViewById(R.id.textView)
        val timerItemDeleteButton: Button = itemView.findViewById(R.id.buttonDelete)

        fun bindTo(name: String?, date: LocalDate?, timeStart: LocalTime?, timeEnd: LocalTime?, notes: String?) {
            timerItemView.text = "$name\n$notes\n${date.toString()}, ${timeStart.toString()}-${timeEnd.toString()}"
        }

        companion object {
            fun create(parent: ViewGroup): TimerViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item_timer, parent, false)
                return TimerViewHolder(view)
            }
        }

        class TimerComparator : DiffUtil.ItemCallback<Timer>() {
            override fun areItemsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }

    /**
     * Called when the Delete button is pressed on a list item. Facilitates the deletion
     * of the Timer from both the list on the backend (through the listener) and the
     * list view in the frontend.
     */
    fun onDelete(position: Int) {
        mListener.timerDelete(getItem(position))
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

}
