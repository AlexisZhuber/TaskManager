package com.example.taskmanager.ui.profile.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ItemPhotoBinding

class PhotoAdapter(private val photos: MutableList<Uri>, private val onDeleteClickListener: (Uri) -> Unit) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemPhotoBinding.bind(itemView)

        fun bind(photoUri: Uri) {
            binding.imageViewPhoto.setImageURI(photoUri)
            binding.buttonDeletePhoto.setOnClickListener {
                onDeleteClickListener(photoUri)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int = photos.size
}
