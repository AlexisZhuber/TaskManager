package com.example.taskmanager.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentRequestedTaskBinding
import com.example.taskmanager.model.Task
import com.example.taskmanager.ui.profile.adapters.TaskAdapter
import com.example.taskmanager.viewmodel.RequestedTaskViewModel

class RequestedTask : Fragment(), TaskAdapter.OnTaskClickListener {

    private var _binding: FragmentRequestedTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RequestedTaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRequestedTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TaskAdapter(emptyList(), this)
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

    override fun onTaskClick(task: Task) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.start_task))
            .setMessage(getString(R.string.start_task_confirmation))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                val intent = Intent(requireContext(), InitTaskFormActivity::class.java)
                intent.putExtra("task", task)
                startActivityForResult(intent, REQUEST_CODE_INIT_TASK)
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_INIT_TASK) {
            viewModel.loadTasks() // Refrescar el RecyclerView al regresar de InitTaskFormActivity
        }
    }

    companion object {
        private const val REQUEST_CODE_INIT_TASK = 1001
    }
}
