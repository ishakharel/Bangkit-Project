package com.ecoloops.ecoloopsapp.ui.page.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ecoloops.ecoloopsapp.databinding.ActivityEditPasswordBinding

class HistoryScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}