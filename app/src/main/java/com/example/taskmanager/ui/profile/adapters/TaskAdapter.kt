package com.example.taskmanager.ui.profile.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.model.Task

class TaskAdapter(private var tasks: List<Task>, private val listener: OnTaskClickListener) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface OnTaskClickListener {
        fun onTaskClick(task: Task)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        private val taskDate: TextView = itemView.findViewById(R.id.taskDate)
        private val taskTime: TextView = itemView.findViewById(R.id.taskTime)
        private val clientName: TextView = itemView.findViewById(R.id.clientName)
        private val clientAddress: TextView = itemView.findViewById(R.id.clientAddress)

        @SuppressLint("SetTextI18n")
        fun bind(task: Task) {
            taskDescription.text = itemView.context.getString(R.string.task_description) + ": " + task.description
            taskDate.text = itemView.context.getString(R.string.task_date) + ": " + task.formattedDate
            taskTime.text = itemView.context.getString(R.string.task_time) + ": " + task.formattedTime
            clientName.text = itemView.context.getString(R.string.client_name) + ": " + task.clientName
            clientAddress.text = itemView.context.getString(R.string.client_address) + ": " + task.clientAddress

            itemView.setOnClickListener {
                listener.onTaskClick(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
