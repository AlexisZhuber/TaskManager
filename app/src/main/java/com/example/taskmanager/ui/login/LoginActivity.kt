package com.example.taskmanager.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskmanager.MainActivity
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ActivityLoginBinding
import com.example.taskmanager.viewmodel.LoginViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        // Check if user is already logged in
        if (firebaseAuth.currentUser != null) {
            // User is logged in, navigate to HomeActivity
            navigateToHome()
            return
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listener for the login button
        binding.loginButton.setOnClickListener {
            val email = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(this, R.string.enter_email_password, Toast.LENGTH_SHORT).show()
            }
        }

        // Observe the login result LiveData from the ViewModel
        loginViewModel.loginResult.observe(this) { result ->
            if (result.first) {
                // Login successful, navigate to HomeActivity
                Toast.makeText(this, R.string.login_successful, Toast.LENGTH_SHORT).show()
                navigateToHome()
            } else {
                // Login failed, show error message
                Toast.makeText(this, getString(R.string.login_failed, result.second), Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for the register text view
        /*binding.textView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }*/
    }

    private fun navigateToHome() {
        fetchAndSaveToken()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun fetchAndSaveToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FirebaseLogs", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            saveTokenToFirestore(token)
        })
    }

    private fun saveTokenToFirestore(token: String) {
        val email = firebaseAuth.currentUser?.email
        val db = FirebaseFirestore.getInstance()
        val workersRef = db.collection("workers")

        if (email != null) {
            workersRef.whereEqualTo("email", email).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        workersRef.document(document.id).update("fcmToken", token)
                    }
                } else {
                    Log.w("FirebaseLogs", "Error getting documents.", task.exception)
                }
            }
        }
    }
}
