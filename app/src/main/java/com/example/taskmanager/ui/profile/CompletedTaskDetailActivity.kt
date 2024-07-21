package com.example.taskmanager.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ActivityCompletedTaskDetailBinding
import com.example.taskmanager.model.FTask
import com.example.taskmanager.ui.profile.adapters.CompletedTaskPhotoAdapter

class CompletedTaskDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompletedTaskDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val task = intent.getParcelableExtra<FTask>("task")

        task?.let {
            binding.taskDescription.text = it.description
            binding.taskStartDate.text = getString(R.string.task_start_date) + ": " + it.formattedStartDate
            binding.taskStartTime.text = getString(R.string.task_start_time) + ": " + it.formattedStartTime
            binding.taskEndDate.text = getString(R.string.task_end_date) + ": " + it.formattedDate
            binding.taskEndTime.text = getString(R.string.task_end_time) + ": " + it.formattedTime

            val photoAdapter = CompletedTaskPhotoAdapter(it.photoUrls)
            binding.recyclerViewPhotos.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewPhotos.adapter = photoAdapter
        }
    }
}
