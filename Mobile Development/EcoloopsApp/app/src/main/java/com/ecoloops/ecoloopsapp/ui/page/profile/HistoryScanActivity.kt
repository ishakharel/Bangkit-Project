package com.ecoloops.ecoloopsapp.ui.page.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.remote.response.ListHistoryItem
import com.ecoloops.ecoloopsapp.databinding.ActivityHistoryScanBinding
import com.ecoloops.ecoloopsapp.ui.auth.login.LoginActivity
import com.ecoloops.ecoloopsapp.ui.page.profile.adapter.ListHistoryAdapter
import com.ecoloops.ecoloopsapp.ui.page.profile.model.ListHistoryVM

var INI_TOKEN = ""

class HistoryScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryScanBinding
    private val listStoryViewModel by viewModels<ListHistoryVM>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreference = LoginPreference(this@HistoryScanActivity)
        val sharedPreferences = userPreference.getUser()

        if (sharedPreferences.token == "") {
            val intent = Intent(this@HistoryScanActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            INI_TOKEN = sharedPreferences.token.toString()
            listStoryViewModel.getHistories("Bearer ${sharedPreferences.token}")
        }

        listStoryViewModel.histories.observe(this) {
            val adapter = ListHistoryAdapter()
            adapter.setList(it.data as ArrayList<ListHistoryItem>)
            binding.historyRv.layoutManager = LinearLayoutManager(this@HistoryScanActivity)
            binding.historyRv.adapter = adapter
        }

        listStoryViewModel.getHistories(sharedPreferences.token.toString())

        listStoryViewModel.isLoading.observe(this) {
            binding.isLoading.visibility = if (it) android.view.View.VISIBLE else android.view.View.GONE
        }
    }
}