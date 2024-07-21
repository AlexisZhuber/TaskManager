package com.example.taskmanager.ui.profile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taskmanager.R
import com.example.taskmanager.model.FTask

class CompletedTaskAdapter(private var tasks: List<FTask>, private val listener: OnTaskClickListener) : RecyclerView.Adapter<CompletedTaskAdapter.TaskViewHolder>() {

    interface OnTaskClickListener {
        fun onTaskClick(task: FTask)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        private val taskDate: TextView = itemView.findViewById(R.id.taskDate)
        private val taskTime: TextView = itemView.findViewById(R.id.taskTime)
        private val taskImage: ImageView = itemView.findViewById(R.id.taskImage)

        fun bind(task: FTask) {
            taskDescription.text = task.description
            taskDate.text = itemView.context.getString(R.string.task_date) + ": " + task.formattedStartDate
            taskTime.text = itemView.context.getString(R.string.task_time) + ": " + task.formattedStartTime

            // Seleccionar una imagen aleatoria
            val randomImageUrl = task.photoUrls.randomOrNull()
            if (randomImageUrl != null) {
                Glide.with(itemView.context).load(randomImageUrl).into(taskImage)
            } else {
                taskImage.setImageResource(R.drawable.logo)
            }

            itemView.setOnClickListener {
                listener.onTaskClick(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_completed_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<FTask>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
