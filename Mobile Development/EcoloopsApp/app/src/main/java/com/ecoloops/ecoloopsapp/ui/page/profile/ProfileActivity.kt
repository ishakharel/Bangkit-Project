package com.ecoloops.ecoloopsapp.ui.page.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.data.model.LoginRequest
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.preference.UploadWastePreference
import com.ecoloops.ecoloopsapp.data.remote.response.DashboardResponse
import com.ecoloops.ecoloopsapp.data.remote.response.ListHistoryResponse
import com.ecoloops.ecoloopsapp.data.remote.response.LoginResponse
import com.ecoloops.ecoloopsapp.data.remote.response.LogoutResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.ActivityProfileBinding
import com.ecoloops.ecoloopsapp.ui.auth.login.LoginActivity
import com.ecoloops.ecoloopsapp.ui.page.home.HomeActivity
import com.ecoloops.ecoloopsapp.ui.page.notification.NotificationActivity
import com.ecoloops.ecoloopsapp.ui.page.reward.RewardActivity
import com.ecoloops.ecoloopsapp.ui.scan.UploadWasteActivity
import com.ecoloops.ecoloopsapp.utils.showAlert
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userProfilePreference = LoginPreference(this)
        val userProfile = userProfilePreference.getUser()

        dashboardData(userProfile.token)

        binding.fab.setOnClickListener {
            val intent = Intent(this@ProfileActivity, UploadWasteActivity::class.java)
            startActivity(intent)
        }



        if(userProfile.image != ""){
            Glide.with(this@ProfileActivity)
                .load(userProfile.image)
                .fitCenter()
                .into(binding.circleIvAvatar)
        }

        binding.tvFullName.text = userProfile.name
        binding.tvEmail.text = userProfile.email

        binding.editProfileCV.setOnClickListener{
            val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.editPasswordCV.setOnClickListener{
            val intent = Intent(this@ProfileActivity, EditPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.historyCV.setOnClickListener{
            val intent = Intent(this@ProfileActivity, HistoryScanActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.logoutCV.setOnClickListener {
            userProfilePreference.clearUser()
            binding.tvLogout.isEnabled = false
            binding.tvLogout.text = "Loading..."

            val apiClient = ApiConfig()

            val apiService = apiClient.createApiService()

            val call = apiService.logout("Bearer ${userProfile.token}")

            call.enqueue(object : Callback<LogoutResponse> {
                override fun onResponse(
                    call: Call<LogoutResponse>,
                    response: Response<LogoutResponse>
                ) {
                    if (response.isSuccessful) {
                        val logoutResponse = response.body()

                        val logout = userProfilePreference.clearUser()

                        if (logout) {
                            Toast.makeText(this@ProfileActivity, logoutResponse?.message, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody.toString())
                        val message = jsonObject.getString("message")
                        showAlert(this@ProfileActivity, message)


                    }
                    binding.tvLogout.isEnabled = true
                    binding.tvLogout.text = "Login"

                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    showAlert(this@ProfileActivity, "This is a simple alertis.")
                    Log.e("RegisterActivity", "onFailure: ${t.message}")
                    binding.tvLogout.isEnabled = true
                    binding.tvLogout.text = "Login"
                }

            })

        }

        bottomNavigation = binding.bottomNavigationView
        bottomNavigation.background = null

        val view: View = bottomNavigation.findViewById(R.id.profile)
        view.performClick()

        val reward: View = bottomNavigation.findViewById(R.id.reward)
        reward.setOnClickListener{
            val intent = Intent(this@ProfileActivity, RewardActivity::class.java)
            startActivity(intent)
        }

        val notification: View = bottomNavigation.findViewById(R.id.notification)
        notification.setOnClickListener{
            val intent = Intent(this@ProfileActivity, NotificationActivity::class.java)
            startActivity(intent)
        }

        val home: View = bottomNavigation.findViewById(R.id.home)
        home.setOnClickListener{
            val intent = Intent(this@ProfileActivity, HomeActivity::class.java)
            startActivity(intent)
        }


    }

    fun dashboardData(token: String?){
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
                    binding.countRewardTv.text = dashboardResponse?.data?.rewards.toString()
                    binding.countScanTv.text = dashboardResponse?.data?.scan.toString()
                    binding.countPointTv.text = dashboardResponse?.data?.points.toString()

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