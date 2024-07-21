package com.example.taskmanager.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ActivityRegisterBinding
import com.example.taskmanager.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listener for the register button
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerViewModel.register(email, password)
            } else {
                Toast.makeText(this, R.string.enter_email_password, Toast.LENGTH_SHORT).show()
            }
        }

        // Observe the registration result LiveData from the ViewModel
        registerViewModel.registerResult.observe(this) { result ->
            if (result.first) {
                // Registration successful, navigate to the login activity
                Toast.makeText(this, R.string.registration_successful, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                // Registration failed, show error message
                Toast.makeText(this, getString(R.string.registration_failed, result.second), Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for the register text view
        binding.textView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
