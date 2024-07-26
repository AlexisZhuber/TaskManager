package com.example.taskmanager.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentDashboardBinding
import com.example.taskmanager.viewmodel.DashboardViewModel

class Dashboard : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.completionRate.observe(viewLifecycleOwner, Observer { completionRate ->
            binding.progressBar.progress = completionRate.toInt()
            binding.progressText.text = getString(R.string.completion_rate, completionRate.toInt())
        })

        viewModel.totalTasks.observe(viewLifecycleOwner, Observer { totalTasks ->
            binding.totalTasksLabel.text = getString(R.string.total_tasks, totalTasks)
        })

        viewModel.completedTasks.observe(viewLifecycleOwner, Observer { completedTasks ->
            binding.completedTasksLabel.text = getString(R.string.completed_tasks, completedTasks)
        })

        viewModel.averageTaskDuration.observe(viewLifecycleOwner, Observer { averageTaskDuration ->
            binding.averageTaskDurationLabel.text = getString(R.string.average_task_duration, averageTaskDuration)
        })

        viewModel.taskDurations.observe(viewLifecycleOwner, Observer { taskDurations ->
            binding.barChart.apply {
                animation.duration = 1000L
                animate(taskDurations)
            }
        })

        viewModel.bitcoinPrice.observe(viewLifecycleOwner, Observer { bitcoinPrice ->
            binding.bitcoinPriceLabel.text = getString(R.string.bitcoin_price, bitcoinPrice.bpi.USD.rate)
        })

        viewModel.loadDashboardData()
        viewModel.fetchBitcoinPrice()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
