package com.ecoloops.ecoloopsapp.ui.page.notification.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ecoloops.ecoloopsapp.R

class NotificationAdapter(private val notificationList: List<NotificationItem>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_notification_title)
        val message: TextView = itemView.findViewById(R.id.tv_notification_message)
        val date: TextView = itemView.findViewById(R.id.tv_notification_date)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.title.text = notificationList[position].title
        holder.message.text = notificationList[position].message
        holder.date.text = notificationList[position].date
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

}