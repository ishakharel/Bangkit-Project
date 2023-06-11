package com.ecoloops.ecoloopsapp.ui.scan

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ecoloops.ecoloopsapp.data.preference.UploadWastePreference
import com.ecoloops.ecoloopsapp.databinding.ActivityResultWasteBinding
import com.ecoloops.ecoloopsapp.ui.page.home.HomeActivity
import com.ecoloops.ecoloopsapp.ui.page.profile.ProfileActivity
import com.ecoloops.ecoloopsapp.utils.withDateFormat

class ResultWasteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultWasteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultWasteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener{
            val intent = Intent(this@ResultWasteActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@ResultWasteActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        val uploadWastePreferences = UploadWastePreference(this)
        val uploadData = uploadWastePreferences.getUploadWaste()


        Glide.with(this@ResultWasteActivity)
            .load(uploadData.image)
            .fitCenter()
            .into(binding.wasteImageView)

        binding.titlePage.text = uploadData.name
        binding.nameTextView.text = uploadData.name
        binding.dateTextView.text = uploadData.date?.withDateFormat()
        binding.poinTextView.text = "+${uploadData.points.toString()}"
        binding.categoryTextView.text = uploadData.category
        binding.descriptionTextView.text = uploadData.description_recycle

    }
}