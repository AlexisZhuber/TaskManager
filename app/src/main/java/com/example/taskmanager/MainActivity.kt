package com.example.taskmanager

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanager.ui.login.LoginActivity
import com.example.taskmanager.ui.profile.CompletedTask
import com.example.taskmanager.ui.profile.Dashboard
import com.example.taskmanager.ui.profile.RequestedTask
import com.example.taskmanager.ui.profile.Settings
import com.example.taskmanager.ui.profile.StartedTask
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.navView.setNavigationItemSelectedListener(this)
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        firebaseAuth = FirebaseAuth.getInstance()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Dashboard()).commit()
            binding.navView.setCheckedItem(R.id.nav_home)
        }

        // Llamar a requequestPermission aquí
        requequestPermission()
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            fetchFirebaseToken()
        }
    }

    private fun fetchFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FirebaseLogs", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.w("FirebaseLogs", "Fetching FCM registration token: $token")
        })
    }

    fun requequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Permiso ya concedido, registrar el token en el Logcat
                fetchFirebaseToken()
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // Mostrar alguna explicación al usuario si es necesario
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            fetchFirebaseToken()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Dashboard()).commit()
            R.id.nav_requested -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RequestedTask()).commit()
            R.id.nav_started -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StartedTask()).commit()
            R.id.nav_completed -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CompletedTask()).commit()
            R.id.nav_settings -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Settings()).commit()
            R.id.nav_logout -> {
                firebaseAuth.signOut()
                Toast.makeText(this, R.string.nav_logout, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
