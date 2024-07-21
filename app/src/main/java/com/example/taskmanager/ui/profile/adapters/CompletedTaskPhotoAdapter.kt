package com.example.taskmanager.ui.profile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taskmanager.R

class CompletedTaskPhotoAdapter(private val photoUrls: List<String>) : RecyclerView.Adapter<CompletedTaskPhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)

        fun bind(url: String) {
            Glide.with(itemView.context).load(url).into(photoImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_completed_task_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoUrls[position])
    }

    override fun getItemCount(): Int = photoUrls.size
}
