package com.example.taskmanager.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RequestedTaskViewModel : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        loadTasks()
    }

    fun loadTasks() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("tasks")
                .whereArrayContains("workerIds", currentUser.uid)
                .whereEqualTo("started", false) // Añadir esta condición
                .get()
                .addOnSuccessListener { result ->
                    val taskList = mutableListOf<Task>()
                    for (document in result) {
                        val task = document.toObject(Task::class.java).apply {
                            idTask = document.id
                        }
                        loadClientInfo(task)
                        taskList.add(task)
                    }
                    _tasks.postValue(taskList)
                }
                .addOnFailureListener { exception ->
                    Log.e("RequestedTaskViewModel", "Error getting tasks", exception)
                }
        } else {
            Log.w("RequestedTaskViewModel", "No user is currently logged in")
        }
    }

    private fun loadClientInfo(task: Task) {
        db.collection("clients")
            .document(task.clientId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    task.clientName = document.getString("name") ?: ""
                    task.clientAddress = document.getString("address") ?: ""
                    // Actualizar LiveData después de obtener la información del cliente
                    _tasks.postValue(_tasks.value)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("RequestedTaskViewModel", "Error getting client info", exception)
            }
    }
}
