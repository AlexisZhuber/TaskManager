package com.example.taskmanager.model

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import android.os.Parcel
import android.os.Parcelable

data class Task(
    var idTask: String = "",
    val description: String = "",
    val date: Timestamp? = null,
    val clientId: String = "",
    var clientName: String = "",
    var clientAddress: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idTask)
        parcel.writeString(description)
        parcel.writeParcelable(date, flags)
        parcel.writeString(clientId)
        parcel.writeString(clientName)
        parcel.writeString(clientAddress)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }

    val formattedDate: String
        get() = formatDate(date)

    val formattedTime: String
        get() = formatTime(date)

    private fun formatDate(timestamp: Timestamp?): String {
        if (timestamp == null) return ""
        val date = timestamp.toDate()
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return outputFormat.format(date)
    }

    private fun formatTime(timestamp: Timestamp?): String {
        if (timestamp == null) return ""
        val date = timestamp.toDate()
        val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return outputFormat.format(date)
    }
}
