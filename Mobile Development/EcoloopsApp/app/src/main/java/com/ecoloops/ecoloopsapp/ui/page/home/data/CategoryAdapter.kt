package com.ecoloops.ecoloopsapp.ui.page.home.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ecoloops.ecoloopsapp.R

class CategoryAdapter(private val categoryList: List<CategoryItem>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.iv_categoryLogo)
        val title: TextView = itemView.findViewById(R.id.tv_logo_categoryTitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.logo.setImageResource(categoryList[position].logo)
        holder.title.text = categoryList[position].title
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}