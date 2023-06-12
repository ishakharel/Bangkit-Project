package com.ecoloops.ecoloopsapp.ui.page.home.adapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.preference.UploadWastePreference
import com.ecoloops.ecoloopsapp.data.remote.response.DetailCategoryResponse
import com.ecoloops.ecoloopsapp.data.remote.response.ListCategoryItem
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.CategoryItemBinding
import com.ecoloops.ecoloopsapp.ui.page.home.DetailCategoryActivity
import com.ecoloops.ecoloopsapp.ui.scan.ResultWasteActivity
import com.ecoloops.ecoloopsapp.utils.showAlert
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
            val apiClient = ApiConfig()
            val apiService = apiClient.createApiService()

            val id = story.id.toString()

            val userPreference = LoginPreference(holder.itemView.context)
            val sharedPreferences = userPreference.getUser()

            val call = apiService.getCategoriesDetail("Bearer ${sharedPreferences.token}", id)

            call.enqueue(object : Callback<DetailCategoryResponse> {
                override fun onResponse(
                    call: Call<DetailCategoryResponse>,
                    response: Response<DetailCategoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val id = story.id.toString()
                        val name = responseBody?.data?.name.toString()
                        val category = responseBody?.data?.category.toString()
                        val descriptionRecycle = responseBody?.data?.description_recycle.toString()
                        val description = responseBody?.data?.description.toString()
                        val points = responseBody?.data?.points.toString()
                        val image = responseBody?.data?.image.toString()

                        val intent = Intent(holder.itemView.context, DetailCategoryActivity::class.java)
                        intent.putExtra("id", id)
                        intent.putExtra("name", name)
                        intent.putExtra("category", category)
                        intent.putExtra("descriptionRecycle", descriptionRecycle)
                        intent.putExtra("description", description)
                        intent.putExtra("points", points)
                        intent.putExtra("image", image)
                        holder.itemView.context.startActivity(intent)

                    } else {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody.toString())
                        val message = jsonObject.getString("message")
                        showAlert(holder.itemView.context, message)
                    }

                }

                override fun onFailure(call: Call<DetailCategoryResponse>, t: Throwable) {
                    showAlert(holder.itemView.context, "This is a simple alertis.")
                    Log.e("Detail Category", "onFailure: ${t.message}")
                }

            })
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