package com.ecoloops.ecoloopsapp.ui.page.reward.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.preference.UploadWastePreference
import com.ecoloops.ecoloopsapp.data.remote.response.ListMerchItem
import com.ecoloops.ecoloopsapp.databinding.RewardItemBinding

class ListMerchAdapter () : RecyclerView.Adapter<ListMerchAdapter.ViewHolder>() {
    val stories = ArrayList<ListMerchItem>()

    fun setList(list: List<ListMerchItem>) {
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
        val binding = RewardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = stories[position]
        holder.name.text = story.name
        holder.stok.text = story.stok.toString()
        holder.points.text = story.points.toString()
        Glide.with(holder.image.context)
            .load(story.image)
            .into(holder.image)

        holder.itemView.setOnClickListener {}
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    class ViewHolder(binding: RewardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvRewardName
        val points = binding.tvRewardPoints
        val stok = binding.tvRewardStock
        val image = binding.ivReward
    }



}