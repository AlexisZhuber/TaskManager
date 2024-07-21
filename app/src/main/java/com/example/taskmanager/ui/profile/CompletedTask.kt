package com.example.taskmanager.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentCompletedTaskBinding
import com.example.taskmanager.model.FTask
import com.example.taskmanager.ui.profile.adapters.CompletedTaskAdapter
import com.example.taskmanager.viewmodel.CompletedTaskViewModel

class CompletedTask : Fragment(), CompletedTaskAdapter.OnTaskClickListener {

    private var _binding: FragmentCompletedTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompletedTaskViewModel by viewModels()
    private lateinit var adapter: CompletedTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompletedTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CompletedTaskAdapter(emptyList(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            adapter.updateTasks(tasks)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTaskClick(task: FTask) {
        val intent = Intent(requireContext(), CompletedTaskDetailActivity::class.java)
        intent.putExtra("task", task)
        startActivity(intent)
    }
}
