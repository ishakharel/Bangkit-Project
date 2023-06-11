package com.ecoloops.ecoloopsapp.ui.page.reward

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.remote.response.DashboardResponse
import com.ecoloops.ecoloopsapp.data.remote.response.ListMerchItem
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.ActivityRewardBinding
import com.ecoloops.ecoloopsapp.ui.page.home.HomeActivity
import com.ecoloops.ecoloopsapp.ui.page.notification.NotificationActivity
import com.ecoloops.ecoloopsapp.ui.page.profile.ProfileActivity
import com.ecoloops.ecoloopsapp.ui.page.reward.adapter.ListMerchAdapter
import com.ecoloops.ecoloopsapp.ui.page.reward.model.ListMerchVM
import com.ecoloops.ecoloopsapp.ui.scan.UploadWasteActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RewardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRewardBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private val listMerchViewModel by viewModels<ListMerchVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            val intent = Intent(this@RewardActivity, UploadWasteActivity::class.java)
            startActivity(intent)
        }

        bottomNavigation = binding.bottomNavigationView
        bottomNavigation.background = null

        val view: View = bottomNavigation.findViewById(R.id.reward)
        view.performClick()

        val home: View = bottomNavigation.findViewById(R.id.home)
        home.setOnClickListener{
            val intent = Intent(this@RewardActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val notification: View = bottomNavigation.findViewById(R.id.notification)
        notification.setOnClickListener{
            val intent = Intent(this@RewardActivity, NotificationActivity::class.java)
            startActivity(intent)
            finish()
        }

        val profile: View = bottomNavigation.findViewById(R.id.profile)
        profile.setOnClickListener{
            val intent = Intent(this@RewardActivity, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@RewardActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        val userPreference = LoginPreference(this@RewardActivity)
        val sharedPreferences = userPreference.getUser()

        dashboardData(sharedPreferences.token)

        listMerchViewModel.getListMerch(sharedPreferences.token)

        listMerchViewModel.merch.observe(this) {
            val adapter = ListMerchAdapter()
            adapter.setList(it.data as ArrayList<ListMerchItem>)
            binding.rvReward.layoutManager = LinearLayoutManager(this@RewardActivity)
            binding.rvReward.adapter = adapter
        }

        listMerchViewModel.getListMerch(sharedPreferences.token)

    }

    private fun dashboardData(token: String?){
        val apiClient = ApiConfig()
        val apiService = apiClient.createApiService()
        val call = apiService.getDashboard("Bearer $token")

        call.enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(
                call: Call<DashboardResponse>,
                response: Response<DashboardResponse>
            ) {
                if (response.isSuccessful) {
                    val dashboardResponse = response.body()
                    binding.tvPoint.text = "${dashboardResponse?.data?.points.toString()} Points"

                } else {
                    val errorBody = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorBody)
                    val message = jsonObject.getString("message")
                    Log.d("HistoryActivity", "onResponses: ${message}")
                }

            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                Log.e("HistoryActivity", "onFailure: ${t.message}")
            }

        })
    }
}