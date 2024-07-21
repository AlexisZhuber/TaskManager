package com.example.taskmanager.model

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Locale

data class STask(
    var idTask: String = "",
    val description: String = "",
    val timestamp: String = "",
    val workerIds: List<String> = emptyList(),
    val photoUrls: List<String> = emptyList(),
    var completed: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idTask)
        parcel.writeString(description)
        parcel.writeString(timestamp)
        parcel.writeStringList(workerIds)
        parcel.writeStringList(photoUrls)
        parcel.writeByte(if (completed) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<STask> {
        override fun createFromParcel(parcel: Parcel): STask {
            return STask(parcel)
        }

        override fun newArray(size: Int): Array<STask?> {
            return arrayOfNulls(size)
        }
    }

    val formattedDate: String
        get() = formatDate(timestamp)

    val formattedTime: String
        get() = formatTime(timestamp)

    private fun formatDate(timestamp: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(timestamp)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            ""
        }
    }

    private fun formatTime(timestamp: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val date = inputFormat.parse(timestamp)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            ""
        }
    }
}
