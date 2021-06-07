package com.example.imgedit.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imgedit.R
import com.example.imgedit.dataBase.entity.EditedImageModel
import kotlinx.android.synthetic.main.item_image_history.view.*

class ImageAdapter() : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<EditedImageModel>() {
        override fun areItemsTheSame(
            oldItem: EditedImageModel,
            newItem: EditedImageModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: EditedImageModel,
            newItem: EditedImageModel
        ): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_image_history,
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((Uri) -> Unit)? = null

    fun setOnItemClickListener(listener: (Uri) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.itemView.apply {
            tv_desc.text = item.text
            Glide.with(this).load(item.image).into(iv_previous_image)
            setOnClickListener {
                onItemClickListener?.let { it(item.image) }
            }
        }
    }
}
