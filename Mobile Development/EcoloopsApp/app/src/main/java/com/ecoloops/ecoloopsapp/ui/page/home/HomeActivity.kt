package com.ecoloops.ecoloopsapp.ui.page.home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.remote.response.ListCategoryItem
import com.ecoloops.ecoloopsapp.data.remote.response.ListHistoryItem
import com.ecoloops.ecoloopsapp.databinding.ActivityHomeBinding
import com.ecoloops.ecoloopsapp.ui.page.home.adapter.ListCategoryAdapter
import com.ecoloops.ecoloopsapp.ui.page.home.model.ListCategoryVM
import com.ecoloops.ecoloopsapp.ui.page.notification.NotificationActivity
import com.ecoloops.ecoloopsapp.ui.page.profile.ProfileActivity
import com.ecoloops.ecoloopsapp.ui.page.profile.adapter.ListHistoryAdapter
import com.ecoloops.ecoloopsapp.ui.page.profile.model.ListHistoryVM
import com.ecoloops.ecoloopsapp.ui.page.reward.RewardActivity
import com.ecoloops.ecoloopsapp.ui.scan.UploadWasteActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private val listCategoryViewModel by viewModels<ListCategoryVM>()


    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        val userPreference = LoginPreference(this@HomeActivity)
        val sharedPreferences = userPreference.getUser()

        binding.userName.text = sharedPreferences.name
        binding.ePoints.text = sharedPreferences.points.toString()
        if(sharedPreferences.image != ""){
            Glide.with(this@HomeActivity)
                .load(sharedPreferences.image)
                .fitCenter()
                .into(binding.profileImage)
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this@HomeActivity, UploadWasteActivity::class.java)
            startActivity(intent)
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (sharedPreferences.token != "") {
                    finishAffinity()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        bottomNavigation = binding.bottomNavigationView
        bottomNavigation.background = null

        val view: View = bottomNavigation.findViewById(R.id.home)
        view.performClick()

        val reward: View = bottomNavigation.findViewById(R.id.reward)
        reward.setOnClickListener{
            val intent = Intent(this@HomeActivity, RewardActivity::class.java)
            startActivity(intent)
        }

        val notification: View = bottomNavigation.findViewById(R.id.notification)
        notification.setOnClickListener{
            val intent = Intent(this@HomeActivity, NotificationActivity::class.java)
            startActivity(intent)
        }

        val profile: View = bottomNavigation.findViewById(R.id.profile)
        profile.setOnClickListener{
            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        listCategoryViewModel.getCategories()

        listCategoryViewModel.categories.observe(this) {
            val adapter = ListCategoryAdapter()
            adapter.setList(it.data as ArrayList<ListCategoryItem>)
            binding.rvCategory.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.rvCategory.adapter = adapter
        }

        listCategoryViewModel.getCategories()

    }
}