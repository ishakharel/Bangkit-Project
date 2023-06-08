package com.ecoloops.ecoloopsapp.ui.page.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.preference.UploadWastePreference
import com.ecoloops.ecoloopsapp.databinding.ActivityProfileBinding
import com.ecoloops.ecoloopsapp.ui.auth.login.LoginActivity
import com.ecoloops.ecoloopsapp.ui.page.home.HomeActivity
import com.ecoloops.ecoloopsapp.ui.page.notification.NotificationActivity
import com.ecoloops.ecoloopsapp.ui.page.reward.RewardActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userProfilePreference = LoginPreference(this)
        val userProfile = userProfilePreference.getUser()

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
        }

        binding.logoutCV.setOnClickListener {
            val logout = userProfilePreference.clearUser()
            if (logout) {
                Toast.makeText(this@ProfileActivity, "Logout Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                intent.putExtra("isLogout", true)
                startActivity(intent)
                finish()
            }

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
}