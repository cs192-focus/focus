package com.focus.focusproductivitymanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.LocalTime

/**
 * The display class for the task list. Adapts the task list data so it can be displayed in the UI.
 */
class TaskListAdapter(context: Context, listener: OnTaskDelete): ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskViewHolder.TaskComparator()) {

    // Enables the Activity to respond when something is clicked on the list itself
    // (in this case, when the Delete button is pressed)
    val mListener: OnTaskDelete = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // When the UI is initialized with the task list data, fill each item in the list with
        // the needed information. The TaskViewHolder class facilitates the insertion of
        // data and interactivity into the UI.
        val current = getItem(position)
        holder.bindTo(current.name, current.notes, current.dueDate, current.dueTime, current.priority, current.isComplete)
        // Links the delete button to the delete functionality.
        holder.taskItemDeleteButton.setOnClickListener { onDelete(position) }
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskItemView: TextView = itemView.findViewById(R.id.textView)
        val taskItemDeleteButton: Button = itemView.findViewById(R.id.buttonDelete)

        fun bindTo(name: String?, notes: String?, dueDate: LocalDate?, dueTime: LocalTime?, priority: Int?, isComplete: Boolean?) {
            taskItemView.text = "$name\n$notes\n${dueDate.toString()}, ${dueTime.toString()}\nPriority $priority\nComplete: ${isComplete.toString()}"
        }

        companion object {
            fun create(parent: ViewGroup): TaskViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return TaskViewHolder(view)
            }
        }

        class TaskComparator : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }

    /**
     * Called when the Delete button is pressed on a list item. Facilitates the deletion
     * of the Task from both the list on the backend (through the listener) and the
     * list view in the frontend.
     */
    fun onDelete(position: Int) {
        mListener.taskDelete(getItem(position))
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

}
