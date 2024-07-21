package com.example.taskmanager.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.model.STask
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StartedTaskViewModel : ViewModel() {

    private val _tasks = MutableLiveData<List<STask>>()
    val tasks: LiveData<List<STask>> = _tasks

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        loadTasks()
    }

    fun loadTasks() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("startedtask")
                .whereArrayContains("workerIds", currentUser.uid)
                .get()
                .addOnSuccessListener { result ->
                    val taskList = mutableListOf<STask>()
                    for (document in result) {
                        val task = document.toObject(STask::class.java).apply {
                            idTask = document.id
                        }
                        if (document.getBoolean("completed") != true) {
                            taskList.add(task)
                        }
                    }
                    _tasks.postValue(taskList)
                }
                .addOnFailureListener { exception ->
                    Log.e("StartedTaskViewModel", "Error getting tasks", exception)
                }
        } else {
            Log.w("StartedTaskViewModel", "No user is currently logged in")
        }
    }
}
