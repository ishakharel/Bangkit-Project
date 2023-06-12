package com.ecoloops.ecoloopsapp.ui.page.profile.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.remote.response.ListHistoryItem
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.ItemHistoryBinding
import com.ecoloops.ecoloopsapp.ui.scan.ResultWasteActivity
import com.ecoloops.ecoloopsapp.utils.showAlert
import com.ecoloops.ecoloopsapp.utils.withDateFormat
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ecoloops.ecoloopsapp.data.preference.UploadWastePreference
import com.ecoloops.ecoloopsapp.data.remote.response.UploadWasteResponse

class ListHistoryAdapter() : RecyclerView.Adapter<ListHistoryAdapter.ViewHolder>() {
    val stories = ArrayList<ListHistoryItem>()

    fun setList(list: List<ListHistoryItem>) {
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
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = stories[position]
        holder.title.text = story.name
        holder.category.text = story.category
        holder.date.text = story.date?.withDateFormat()
        holder.point.text = " +${story.points.toString()}"
        Glide.with(holder.image.context)
            .load(story.image)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val apiClient = ApiConfig()
            val apiService = apiClient.createApiService()

            val id = story.id.toString()

            val userPreference = LoginPreference(holder.itemView.context)
            val uploadWastePreferences = UploadWastePreference(holder.itemView.context)
            val sharedPreferences = userPreference.getUser()

            val call = apiService.getHistoriesDetail("Bearer ${sharedPreferences.token}", id)

            call.enqueue(object : Callback<UploadWasteResponse> {
                override fun onResponse(
                    call: Call<UploadWasteResponse>,
                    response: Response<UploadWasteResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val id = story.id.toString()
                        val name = responseBody?.data?.name.toString()
                        val category = responseBody?.data?.category.toString()
                        val descriptionRecycle = responseBody?.data?.description_recycle.toString()
                        val date = responseBody?.data?.date.toString()
                        val points = responseBody?.data?.points.toString().toInt()
                        val image = responseBody?.data?.image.toString()

                        uploadWastePreferences.setUploadWaste(id, name, category, descriptionRecycle, date, points, image)

                        val intent = Intent(holder.itemView.context, ResultWasteActivity::class.java)
                        holder.itemView.context.startActivity(intent)


                    } else {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody.toString())
                        val message = jsonObject.getString("message")
                        showAlert(holder.itemView.context, message)


                    }

                }

                override fun onFailure(call: Call<UploadWasteResponse>, t: Throwable) {
                    showAlert(holder.itemView.context, "This is a simple alertis.")
                    Log.e("Detail History", "onFailure: ${t.message}")
                }

            })
        }
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    class ViewHolder(binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivAvatar
        val title = binding.tvTitle
        val point = binding.tvPoint
        val category = binding.tvCategory
        val date = binding.tvDate
    }



}