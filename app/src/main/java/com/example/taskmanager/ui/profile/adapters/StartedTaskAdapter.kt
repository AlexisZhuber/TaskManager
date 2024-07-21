package com.example.taskmanager.ui.profile.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.model.STask

class StartedTaskAdapter(private var tasks: List<STask>, private val listener: OnTaskClickListener) : RecyclerView.Adapter<StartedTaskAdapter.TaskViewHolder>() {

    interface OnTaskClickListener {
        fun onTaskClick(task: STask)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        private val taskDate: TextView = itemView.findViewById(R.id.taskDate)
        private val taskTime: TextView = itemView.findViewById(R.id.taskTime)

        @SuppressLint("SetTextI18n")
        fun bind(task: STask) {
            taskDescription.text = itemView.context.getString(R.string.task_description) + ": " + task.description
            taskDate.text = itemView.context.getString(R.string.task_date) + ": " + task.formattedDate
            taskTime.text = itemView.context.getString(R.string.task_time) + ": " + task.formattedTime

            itemView.setOnClickListener {
                listener.onTaskClick(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_started_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTasks(newTasks: List<STask>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
