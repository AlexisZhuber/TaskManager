package com.example.taskmanager.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ActivityInitTaskFormBinding
import com.example.taskmanager.model.Task
import com.example.taskmanager.ui.profile.adapters.PhotoAdapter
import com.example.taskmanager.viewmodel.InitTaskFormViewModel
import java.util.Locale

class InitTaskFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInitTaskFormBinding
    private val viewModel: InitTaskFormViewModel by viewModels()
    private val photos = mutableListOf<Uri>()
    private lateinit var photoAdapter: PhotoAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            takePhoto()
        } else {
            Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            if (photos.size < 5) {
                photos.add(it)
                photoAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, R.string.max_photos, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val getSpeechInput = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val spokenText: String? = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            binding.editTextDescription.setText(spokenText)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        photoAdapter = PhotoAdapter(photos) { uri ->
            photos.remove(uri)
            photoAdapter.notifyDataSetChanged()
        }
        binding.recyclerViewPhotos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPhotos.adapter = photoAdapter

        binding.buttonVoiceInput.setOnClickListener {
            startVoiceRecognitionActivity()
        }

        binding.buttonTakePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        binding.buttonSubmit.setOnClickListener {
            val description = binding.editTextDescription.text.toString()
            val task: Task? = intent.getParcelableExtra("task")
            task?.let {
                showLoading(true)
                viewModel.submitForm(description, photos, it.idTask)
            }
        }

        viewModel.formSubmitResult.observe(this, Observer { result ->
            showLoading(false)
            if (result) {
                setResult(Activity.RESULT_OK)
                Toast.makeText(this, R.string.form_submitted, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, R.string.form_submit_failed, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun takePhoto() {
        getContent.launch("image/*")
    }

    private fun startVoiceRecognitionActivity() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak_now))
        try {
            getSpeechInput.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(this, R.string.voice_input_not_supported, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate)
            binding.loadingContainer.visibility = View.VISIBLE
            binding.progressBar.startAnimation(rotateAnimation)
            binding.editTextDescription.visibility = View.GONE
            binding.buttonVoiceInput.visibility = View.GONE
            binding.buttonTakePhoto.visibility = View.GONE
            binding.recyclerViewPhotos.visibility = View.GONE
            binding.buttonSubmit.visibility = View.GONE
        } else {
            binding.loadingContainer.visibility = View.GONE
            binding.progressBar.clearAnimation()
            binding.editTextDescription.visibility = View.VISIBLE
            binding.buttonVoiceInput.visibility = View.VISIBLE
            binding.buttonTakePhoto.visibility = View.VISIBLE
            binding.recyclerViewPhotos.visibility = View.VISIBLE
            binding.buttonSubmit.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
