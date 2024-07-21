package com.example.taskmanager.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class InitTaskFormViewModel : ViewModel() {

    private val _formSubmitResult = MutableLiveData<Boolean>()
    val formSubmitResult: LiveData<Boolean> = _formSubmitResult

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun submitForm(description: String, photos: List<Uri>, taskId: String) {
        val photoUrls = mutableListOf<String>()
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            if (photos.isNotEmpty()) {
                val uploadTasks = photos.map { uri ->
                    val ref = storage.reference.child("images/$userId/${UUID.randomUUID()}.jpg")
                    ref.putFile(uri).continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let { throw it }
                        }
                        ref.downloadUrl
                    }
                }

                Tasks.whenAllSuccess<Uri>(uploadTasks)
                    .addOnSuccessListener { urls ->
                        photoUrls.addAll(urls.map { it.toString() })
                        saveForm(description, photoUrls, date, taskId)
                    }
                    .addOnFailureListener { exception ->
                        exception.printStackTrace()
                        _formSubmitResult.value = false
                    }
            } else {
                saveForm(description, photoUrls, date, taskId)
            }
        } else {
            _formSubmitResult.value = false
        }
    }

    private fun saveForm(description: String, photoUrls: List<String>, date: String, taskId: String) {
        db.collection("tasks").document(taskId).get().addOnSuccessListener { document ->
            if (document.exists()) {
                val workerIds = document.get("workerIds") as? List<String> ?: emptyList<String>()

                val startedTask = hashMapOf(
                    "description" to description,
                    "photoUrls" to photoUrls,
                    "timestamp" to date,
                    "workerIds" to workerIds,
                    "completed" to false
                )

                db.collection("startedtask")
                    .add(startedTask)
                    .addOnSuccessListener {
                        updateTask(taskId)
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        _formSubmitResult.value = false
                    }
            } else {
                _formSubmitResult.value = false
            }
        }.addOnFailureListener {
            it.printStackTrace()
            _formSubmitResult.value = false
        }
    }

    private fun updateTask(taskId: String) {
        db.collection("tasks").document(taskId)
            .update("started", true)
            .addOnSuccessListener {
                _formSubmitResult.value = true
            }
            .addOnFailureListener {
                it.printStackTrace()
                _formSubmitResult.value = false
            }
    }
}
