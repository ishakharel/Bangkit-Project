package com.ecoloops.ecoloopsapp.ui.scan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ecoloops.ecoloopsapp.data.preference.UploadWastePreference
import com.ecoloops.ecoloopsapp.databinding.ActivityResultWasteBinding

class ResultWasteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultWasteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultWasteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uploadWastePreferences = UploadWastePreference(this)
        val uploadData = uploadWastePreferences.getUploadWaste()

        binding.nameTextView.text = uploadData.name
        binding.dateTextView.text = uploadData.date
        binding.poinTextView.text = uploadData.points.toString()
        binding.categoryTextView.text = uploadData.category
        binding.descriptionTextView.text = uploadData.description_recycle

    }
}