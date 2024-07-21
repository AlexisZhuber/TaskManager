package com.example.taskmanager.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.taskmanager.MainActivity
import com.example.taskmanager.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveTokenToFirestore(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title = remoteMessage.notification?.title ?: "Task Manager"
        val message = remoteMessage.notification?.body ?: "You have a new notification"
        showNotification(title, message)
    }

    private fun showNotification(title: String, message: String) {
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName = getString(R.string.default_notification_channel_name)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = getString(R.string.default_notification_channel_description)
            }
            notificationManager.createNotificationChannel(channel)
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun saveTokenToFirestore(token: String) {
        val email = FirebaseAuth.getInstance().currentUser?.email
        val db = FirebaseFirestore.getInstance()
        val workersRef = db.collection("workers")

        if (email != null) {
            workersRef.whereEqualTo("email", email).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        workersRef.document(document.id).update("token", token)
                    }
                } else {
                    Log.w("FirebaseLogs", "Error getting documents.", task.exception)
                }
            }
        }
    }
}
