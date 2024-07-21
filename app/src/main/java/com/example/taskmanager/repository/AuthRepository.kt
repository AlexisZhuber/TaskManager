package com.example.taskmanager.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, null)
            } else {
                callback(false, task.exception?.message)
            }
        }
    }

    fun changePassword(newPassword: String, callback: (Boolean) -> Unit) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseAuth.signOut()
                        callback(true)
                    } else {
                        callback(false)
                    }
                }
        } else {
            callback(false)
        }
    }

    fun register(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, null)
            } else {
                callback(false, task.exception?.message)
            }
        }
    }
}
