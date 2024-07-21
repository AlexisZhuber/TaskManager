package com.example.taskmanager.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentSettingsBinding
import com.example.taskmanager.ui.login.LoginActivity
import com.example.taskmanager.viewmodel.SettingsViewModel

class Settings : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changePasswordButton.setOnClickListener {
            val newPassword = binding.newPasswordEditText.text.toString()
            if (isValidPassword(newPassword)) {
                showConfirmDialog(newPassword)
            } else {
                Toast.makeText(context, R.string.invalid_password, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.passwordChangeResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(context, R.string.password_change_success, Toast.LENGTH_SHORT).show()
                navigateToLogin()
            } else {
                Toast.makeText(context, R.string.password_change_failed, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
        return password.matches(passwordPattern)
    }

    private fun showConfirmDialog(newPassword: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.confirm_password_change)
            .setMessage(R.string.confirm_password_change)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.changePassword(newPassword)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
