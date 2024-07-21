package com.example.taskmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.model.FTask
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CompletedTaskViewModel : ViewModel() {

    private val _tasks = MutableLiveData<List<FTask>>()
    val tasks: LiveData<List<FTask>> = _tasks

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        loadTasks()
    }

    fun loadTasks() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("finishedtask")
                .whereArrayContains("workerIds", currentUser.uid)
                .get()
                .addOnSuccessListener { result ->
                    val taskList = mutableListOf<FTask>()
                    for (document in result) {
                        val task = document.toObject(FTask::class.java).apply {
                            idTask = document.id
                        }
                        taskList.add(task)
                    }
                    _tasks.postValue(taskList)
                }
                .addOnFailureListener { exception ->
                    // Handle exception
                }
        }
    }
}
