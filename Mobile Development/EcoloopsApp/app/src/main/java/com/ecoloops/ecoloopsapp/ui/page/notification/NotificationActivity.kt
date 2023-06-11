package com.ecoloops.ecoloopsapp.ui.page.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.databinding.ActivityNotificationBinding
import com.ecoloops.ecoloopsapp.ui.page.home.HomeActivity
import com.ecoloops.ecoloopsapp.ui.page.notification.data.NotificationAdapter
import com.ecoloops.ecoloopsapp.ui.page.notification.data.NotificationItem
import com.ecoloops.ecoloopsapp.ui.page.profile.ProfileActivity
import com.ecoloops.ecoloopsapp.ui.page.reward.RewardActivity
import com.ecoloops.ecoloopsapp.ui.scan.UploadWasteActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private val notificationList = ArrayList<NotificationItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            val intent = Intent(this@NotificationActivity, UploadWasteActivity::class.java)
            startActivity(intent)
        }

        bottomNavigation = binding.bottomNavigationView
        bottomNavigation.background = null

        val view: View = bottomNavigation.findViewById(R.id.notification)
        view.performClick()

        val reward: View = bottomNavigation.findViewById(R.id.reward)
        reward.setOnClickListener{
            val intent = Intent(this@NotificationActivity, RewardActivity::class.java)
            startActivity(intent)
            finish()
        }

        val home: View = bottomNavigation.findViewById(R.id.home)
        home.setOnClickListener{
            val intent = Intent(this@NotificationActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val profile: View = bottomNavigation.findViewById(R.id.profile)
        profile.setOnClickListener{
            val intent = Intent(this@NotificationActivity, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@NotificationActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        recyclerView = findViewById(R.id.rv_notification)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        addDataToList()
        val adapter = NotificationAdapter(notificationList)
        recyclerView.adapter = adapter
    }

    private fun addDataToList() {
        notificationList.add(NotificationItem("Selamat datang di aplikasi Ecoloops", "Bersama Ecoloops membuat lingkungan lebih bersih!", "15 Juni 2023"))
        notificationList.add(NotificationItem("Ayo Lengkapi Profilmu!", "Lengkapi profilemu pada menu edit profile untuk pengalaman lebih baik!", "15 Juni 2023"))
        notificationList.add(NotificationItem("Ayo Scan dan Kenali!", "Kamu bisa melakukan scan untuk mengidentifikasi Jenis sampah dan cara mendaur ulang", "16 Juni 2023"))
        notificationList.add(NotificationItem("Mendapatkan E-Point!", "Kamu bisa mendapatkan E-Points untuk setiap melakukan scan sampah", "16 Juni 2023"))
        notificationList.add(NotificationItem("Batas maksimum scan", "Saat ini batas maksimum scan adalah 4 kali scan perharinya", "17 Juni 2023"))
        notificationList.add(NotificationItem("Kenali sampah dan cara daur ulang!", "Kamu bisa mengakses informasi sampah melalui kategori sampah di home", "17 juni 2023"))
    }
}