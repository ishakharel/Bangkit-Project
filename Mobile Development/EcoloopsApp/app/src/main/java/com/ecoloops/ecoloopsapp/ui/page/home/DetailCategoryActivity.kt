package com.ecoloops.ecoloopsapp.ui.page.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ecoloops.ecoloopsapp.databinding.ActivityDetailCategoryBinding

class DetailCategoryActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.titleNameTextView.text = intent.getStringExtra("name")
        binding.descTextView.text = intent.getStringExtra("description")
        binding.categoryTextView.text = intent.getStringExtra("category")
        binding.descriptionRCTextView.text = intent.getStringExtra("descriptionRecycle")
        Glide.with(this@DetailCategoryActivity)
            .load(intent.getStringExtra("image"))
            .fitCenter()
            .into(binding.wasteImageView)
    }

}