package com.ecoloops.ecoloopsapp.ui.page.home.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.preference.UploadWastePreference
import com.ecoloops.ecoloopsapp.data.remote.response.ListCategoryItem
import com.ecoloops.ecoloopsapp.data.remote.response.UploadWasteResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.CategoryItemBinding

class ListCategoryAdapter() : RecyclerView.Adapter<ListCategoryAdapter.ViewHolder>() {
    val stories = ArrayList<ListCategoryItem>()

    fun setList(list: List<ListCategoryItem>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return stories[oldItemPosition].id == list[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return stories[oldItemPosition] == list[newItemPosition]
            }

            override fun getOldListSize(): Int {
                return stories.size
            }

            override fun getNewListSize(): Int {
                return list.size
            }
        })

        diffResult.dispatchUpdatesTo(this)
        stories.clear()
        stories.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = stories[position]
        holder.category.text = story.name
        Glide.with(holder.image.context)
            .load(story.image)
            .into(holder.image)

        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    class ViewHolder(binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivCategoryLogo
        val category = binding.tvLogoCategoryTitle
    }



}