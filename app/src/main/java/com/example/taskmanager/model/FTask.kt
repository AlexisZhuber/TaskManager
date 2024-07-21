package com.example.taskmanager.model

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Locale

data class FTask(
    var idTask: String = "",
    val description: String = "",
    val timestamp: String = "",
    val startedTimestamp: String = "",
    val workerIds: List<String> = emptyList(),
    val photoUrls: List<String> = emptyList()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.createStringArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idTask)
        parcel.writeString(description)
        parcel.writeString(timestamp)
        parcel.writeString(startedTimestamp)
        parcel.writeStringList(workerIds)
        parcel.writeStringList(photoUrls)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FTask> {
        override fun createFromParcel(parcel: Parcel): FTask {
            return FTask(parcel)
        }

        override fun newArray(size: Int): Array<FTask?> {
            return arrayOfNulls(size)
        }
    }

    val formattedStartDate: String
        get() = formatDate(startedTimestamp)

    val formattedStartTime: String
        get() = formatTime(startedTimestamp)

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
